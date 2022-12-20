/*
@Deprecated
 */

package com.loam.stoody.global.managers;

import java.util.List;

@Deprecated
public class PageDataManager {
    public static <T> List<T> getPagedData(List<T> list, int showMax, int currentPage) {
        if(list.isEmpty())
            return list;

        final int totalSize = list.size();
        final int _maxPageCount = (int) Math.ceil(((double) totalSize / showMax));

        if (currentPage > _maxPageCount)
            currentPage = _maxPageCount;
        else if (currentPage < 1)
            currentPage = 1;

        int startIndex = (currentPage - 1 < 0 ? 0 : currentPage - 1) * (showMax - 1);

        int endIndex = (startIndex + showMax) - 1 < 0 ? 0 : (startIndex + showMax) - 1;

        endIndex = endIndex >= (list.size()) ? list.size() : endIndex;

        return list.subList(startIndex, endIndex);
    }
}