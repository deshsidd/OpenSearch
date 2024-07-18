/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.tasks;

import org.opensearch.action.search.SearchPhaseContext;
import org.opensearch.action.search.SearchRequestContext;
import org.opensearch.action.search.SearchRequestOperationsListener;

/**
 * SearchTaskRequestOperationsListener subscriber for operations on search tasks resource usages.
 * Listener ensures to refreshResourceStats on request end capturing the search task resource usage
 * upon request completion.
 *
 */
public final class SearchTaskRequestOperationsListener extends SearchRequestOperationsListener {
    private static SearchTaskRequestOperationsListener instance;
    private final TaskResourceTrackingService taskResourceTrackingService;

    private SearchTaskRequestOperationsListener(TaskResourceTrackingService taskResourceTrackingService) {
        this.taskResourceTrackingService = taskResourceTrackingService;
    }

    public static synchronized SearchTaskRequestOperationsListener getInstance(TaskResourceTrackingService taskResourceTrackingService) {
        if (instance == null) {
            instance = new SearchTaskRequestOperationsListener(taskResourceTrackingService);
        }
        return instance;
    }

    @Override
    public void onRequestEnd(SearchPhaseContext context, SearchRequestContext searchRequestContext) {
        taskResourceTrackingService.refreshResourceStats(context.getTask());
    }
}
