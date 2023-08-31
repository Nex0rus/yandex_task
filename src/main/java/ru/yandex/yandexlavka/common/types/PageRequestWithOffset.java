package ru.yandex.yandexlavka.common.types;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@EqualsAndHashCode
@ToString
public class PageRequestWithOffset implements Pageable {
    @Getter
    private final int limit;

    private final int offset;
    @Getter
    private final Sort sort;
    private PageRequestWithOffset(int offset, int limit, Sort sort) {
        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }

    public static PageRequestWithOffset getInstance(int offset, int limit) {
        return new PageRequestWithOffset(offset, limit, Sort.unsorted());
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Pageable next() {
        return new PageRequestWithOffset((int)getOffset() + getPageSize(), getPageSize(), getSort());
    }

    public PageRequestWithOffset previous() {
        return hasPrevious() ? new PageRequestWithOffset((int)getOffset() - getPageSize(), getPageSize(), getSort()) : this;
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public Pageable first() {
        return new PageRequestWithOffset(0, getPageSize(), getSort());
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}
