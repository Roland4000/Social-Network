package org.example.socialnetworkjavafx.Repositories;

import org.example.socialnetworkjavafx.Entities.Entity;
import org.example.socialnetworkjavafx.Utils.Paging.Page;
import org.example.socialnetworkjavafx.Utils.Paging.Pageable;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
    Page<E> findAllOnPage(Pageable pageable, String user);
}
