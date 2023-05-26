package ru.bardinpetr.itmo.lab5.server.app.utils;

import ru.bardinpetr.itmo.lab5.models.commands.requests.PagingAPICommand;

import java.util.List;
import java.util.stream.Stream;

/**
 * Pagination adapter for db methods
 *
 * @param <T> collection item
 */
public class DBPager<T> {
    public List<T> paginate(Stream<T> input, PagingAPICommand command) {
        if (command.getOffset() < 0 || command.getCount() < 0)
            throw new RuntimeException("Invalid offset/count");

        var base = input.skip(command.getOffset());
        var cnt = command.getCount();
        if (!cnt.equals(PagingAPICommand.FULL_COUNT))
            base = base.limit(cnt);

        var list = base.toList();
        if (list.isEmpty())
            throw new RuntimeException("No elements for such request");

        return list;
    }
}
