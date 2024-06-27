/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.action.mgt.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.action.mgt.ActionMgtException;
import org.wso2.carbon.identity.action.mgt.cache.ActionCacheByType;
import org.wso2.carbon.identity.action.mgt.cache.ActionCacheEntry;
import org.wso2.carbon.identity.action.mgt.cache.ActionTypeCacheKey;
import org.wso2.carbon.identity.action.mgt.dao.ActionManagementDAO;
import org.wso2.carbon.identity.action.mgt.model.Action;
import org.wso2.carbon.identity.action.mgt.model.ActionType;

import java.util.List;

/**
 * This class implements the {@link ActionManagementDAO} interface.
 */
public class CacheBackedActionMgtDAO implements ActionManagementDAO {

    private static final Log LOG = LogFactory.getLog(CacheBackedActionMgtDAO.class);
    private final ActionCacheByType actionCacheByType;
    private final ActionManagementDAO actionManagementDAO;

    public CacheBackedActionMgtDAO(ActionManagementDAO actionManagementDAO) {

        this.actionManagementDAO = actionManagementDAO;
        actionCacheByType = ActionCacheByType.getInstance();
    }

    @Override
    public Action addAction(String actionType, Action action, Integer tenantId)
            throws ActionMgtException {

//        Action actionResponse = actionManagementDAO.addAction(actionType, action, tenantId);
//
//        ActionTypeCacheKey cacheKey = new ActionTypeCacheKey(actionType);
//        ActionCacheEntry entry = actionCacheByType.getValueFromCache(cacheKey, tenantId);
//
//        if (entry != null) {
//            if (LOG.isDebugEnabled()) {
//                LOG.debug("Cache entry found for Action Type " + actionType +
//                        ". Hence modifying the cache with new action.");
//            }
//            List<Action> actions = entry.getActions();
//            actions.add(actionResponse);
//
//            actionCacheByType.clearCacheEntry(cacheKey, tenantId);
//            actionCacheByType.addToCache(cacheKey, new ActionCacheEntry(actions), tenantId);
//        }
//        return actionResponse;
        return actionManagementDAO.addAction(actionType, action, tenantId);
    }

    @Override
    public List<Action> getActionsByActionType(String actionType, Integer tenantId) throws ActionMgtException {

        ActionTypeCacheKey cacheKey = new ActionTypeCacheKey(actionType);
        ActionCacheEntry entry = actionCacheByType.getValueFromCache(cacheKey, tenantId);

        if (entry != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Cache entry found for Action Type " + actionType);
            }
            return entry.getActions();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Cache entry not found for Action Type " + actionType + ". Fetching entry from DB.");
        }

        List<Action> actions = actionManagementDAO.getActionsByActionType(actionType, tenantId);

        if (actions != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Entry fetched from DB for Action Type " + actionType + ". Updating cache.");
            }
            actionCacheByType.addToCache(cacheKey, new ActionCacheEntry(actions), tenantId);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Entry for Action Type " + actionType + " not found in cache or DB.");
            }
        }
        return actions;
    }

    @Override
    public Action updateAction(String actionType, String actionId, Action action,
                               Integer tenantId) throws ActionMgtException {

        actionCacheByType.clearCacheEntry(new ActionTypeCacheKey(actionType), tenantId);
        return actionManagementDAO.updateAction(actionType, actionId, action, tenantId);
    }

    @Override
    public void deleteAction(String actionType, String actionId, Integer tenantId) throws ActionMgtException {

        actionCacheByType.clearCacheEntry(new ActionTypeCacheKey(actionType), tenantId);
        actionManagementDAO.deleteAction(actionType, actionId, tenantId);
    }

    @Override
    public Action activateAction(String actionType, String actionId, Integer tenantId) throws ActionMgtException {

        actionCacheByType.clearCacheEntry(new ActionTypeCacheKey(actionType), tenantId);
        return actionManagementDAO.activateAction(actionType, actionId, tenantId);
    }

    @Override
    public Action deactivateAction(String actionType, String actionId, Integer tenantId) throws ActionMgtException {

        actionCacheByType.clearCacheEntry(new ActionTypeCacheKey(actionType), tenantId);
        return actionManagementDAO.deactivateAction(actionType, actionId, tenantId);
    }

    @Override
    public List<ActionType> getActionTypes(Integer tenantId) throws ActionMgtException {

        return actionManagementDAO.getActionTypes(tenantId);
    }
}
