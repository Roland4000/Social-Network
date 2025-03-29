package org.example.socialnetworkjavafx.Services;

import org.example.socialnetworkjavafx.Entities.Friendship;
import org.example.socialnetworkjavafx.Entities.Network;
import org.example.socialnetworkjavafx.Entities.User;
import org.example.socialnetworkjavafx.Exceptions.AcceptFriendshipException;
import org.example.socialnetworkjavafx.Exceptions.FriendshipAlreadyExistingException;
import org.example.socialnetworkjavafx.Exceptions.FriendshipNotFoundException;
import org.example.socialnetworkjavafx.Factories.FriendshipFactory;
import org.example.socialnetworkjavafx.Repositories.PagingRepository;
import org.example.socialnetworkjavafx.Repositories.Repository;
import org.example.socialnetworkjavafx.Utils.Event.ChangeType;
import org.example.socialnetworkjavafx.Utils.Event.FriendshipEntityChangeEvent;
import org.example.socialnetworkjavafx.Utils.Observer.Observable;
import org.example.socialnetworkjavafx.Utils.Observer.Observer;
import org.example.socialnetworkjavafx.Utils.Paging.Page;
import org.example.socialnetworkjavafx.Utils.Paging.Pageable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

public class NetworkService implements Observable<FriendshipEntityChangeEvent> {
    FriendshipFactory friendshipFactory;
    PagingRepository<Integer, Friendship> friendshipRepository;
    Repository<String, User> userRepository;
    Network network;
    List<Observer<FriendshipEntityChangeEvent>> observers = new ArrayList<>();

    public NetworkService(PagingRepository<Integer, Friendship> friendshipRepository, Repository<String, User> userRepository, Network network) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
        this.network = network;
        friendshipFactory = FriendshipFactory.getInstance();
    }

    public void addFriendship(Friendship friendship) throws Exception {
        Predicate<Friendship> friendshipExists = f -> friendshipRepository.findOne(f.hashCode()).isPresent();

        if (!friendshipExists.test(friendship)) {
            friendshipRepository.save(friendship);
            FriendshipEntityChangeEvent event = new FriendshipEntityChangeEvent(ChangeType.ADD, friendship);
            notifyObservers(event);
        } else {
            throw new FriendshipAlreadyExistingException(friendship);
        }
    }

    public void acceptFriendship(String loggedUser, String username) throws Exception, AcceptFriendshipException {
        Friendship oldFriendship = getAFriendship(loggedUser, username);
        Friendship toAccept = getAFriendship(loggedUser, username);
        if (toAccept.getInitiator().equals(loggedUser)) {
            throw new AcceptFriendshipException();
        }
        toAccept.Accept();
        toAccept.setDateTime(LocalDateTime.now());
        friendshipRepository.update(toAccept);
        FriendshipEntityChangeEvent event = new FriendshipEntityChangeEvent(ChangeType.UPDATE, toAccept, oldFriendship);
        notifyObservers(event);
    }

    public void removeFriendship(String username1, String username2) throws FriendshipNotFoundException {
        Optional<Friendship> toDelete1 = friendshipRepository.findOne(friendshipFactory.createFriendship(username1, username2).hashCode());
        if (toDelete1.isPresent()) {
            friendshipRepository.delete(toDelete1.get().hashCode());
            FriendshipEntityChangeEvent event = new FriendshipEntityChangeEvent(ChangeType.UPDATE, toDelete1.get());
            notifyObservers(event);
        } else{
            throw new FriendshipNotFoundException(username1, username2);
        }
    }

    public List<Friendship> getAllFriendships() {
        Iterable<Friendship> allFriendships = friendshipRepository.findAll();
        List<Friendship> friendships = new ArrayList<>();

        allFriendships.forEach(f -> friendships.add(f));

        return friendships;
    }

    public Page<Friendship> getAllPagedFriendships(Pageable pageable, String user) {
        return friendshipRepository.findAllOnPage(pageable,user);
    }

    public Friendship getAFriendship(String username1, String username2) throws FriendshipNotFoundException {
        Friendship to_search = friendshipFactory.createFriendship(username1, username2);
        Optional<Friendship> friendship =  friendshipRepository.findOne(to_search.hashCode());
        if (friendship.isPresent()) {
            return friendship.get();
        }
        to_search = friendshipFactory.createFriendship(username2, username1);
        friendship =  friendshipRepository.findOne(to_search.hashCode());
        if (friendship.isPresent()) {
            return friendship.get();
        }
        throw new FriendshipNotFoundException(username1, username2);
    }

    public void removeFriendshipsOfDeleted(String username) {
        Iterable<Friendship> allFriendships = friendshipRepository.findAll();
        List<Friendship> toDetlete = new ArrayList<>();

        allFriendships.forEach(f -> {
            if (Objects.equals(f.getUser1(), username)) {
                toDetlete.add(f);
            }
            if (Objects.equals(f.getUser2(), username)) {
                toDetlete.add(f);
            }
        });

        toDetlete.forEach(f -> removeFriendship(f.getUser1(), f.getUser2()));
    }

    public int numberOfCommunities() {
        Iterable users = userRepository.findAll();
        List<User> userList = new ArrayList<>();
        List<Friendship> friendships = getAllFriendships();

        Map<String, List<String>> adjacencyList = new HashMap<>();

        users.forEach(user -> {
            User us = (User) user;
            adjacencyList.put(us.getUsername(), new ArrayList<>());
            userList.add(us);
        });

        for (Friendship friendship : friendships) {
            adjacencyList.get(friendship.getUser1()).add(friendship.getUser2());
            adjacencyList.get(friendship.getUser2()).add(friendship.getUser1());
        }

        Set<String> visited = new HashSet<>();
        int cnt = 0;

        for (User user : userList) {
            if (!visited.contains(user.getUsername())) {
                cnt++;
                dfs(user.getUsername(), adjacencyList, visited);
            }
        }
        return cnt;
    }

    private void dfs(String username, Map<String, List<String>> adjacencyList, Set<String> visited) {
        visited.add(username);
        for (String neighbor : adjacencyList.get(username)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, adjacencyList, visited);
            }
        }
    }

    public List<String> theMostSociableComunity() {
        Iterable users = userRepository.findAll();
        List<User> userList = new ArrayList<>();
        List<Friendship> friendships = getAllFriendships();


        Map<String, List<String>> adjacencyList = new HashMap<>();

        users.forEach(user -> {
            User us = (User) user;
            adjacencyList.put(us.getUsername(), new ArrayList<>());
            userList.add(us);
        });

        for (Friendship friendship : friendships) {
            adjacencyList.get(friendship.getUser1()).add(friendship.getUser2());
            adjacencyList.get(friendship.getUser2()).add(friendship.getUser1());
        }
        List<String> largestCom = new ArrayList<>();

        users = friendshipRepository.findAll();
        for (User user : userList) {
            Set<String> visited = new HashSet<>();
            dfs(user.getUsername(), adjacencyList, visited);
            if (visited.size() > largestCom.size()) {
                largestCom.clear();
                largestCom.addAll(visited);
            }
        }
        return largestCom;
    }

    @Override
    public void addObserver(Observer<FriendshipEntityChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<FriendshipEntityChangeEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(FriendshipEntityChangeEvent event) {
        observers.forEach(observer -> observer.update(event));
    }
}
