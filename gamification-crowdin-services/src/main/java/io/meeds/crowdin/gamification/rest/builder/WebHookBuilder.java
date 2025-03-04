/*
 * This file is part of the Meeds project (https://meeds.io/).
 *
 * Copyright (C) 2020 - 2024 Meeds Lab contact@meedslab.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package io.meeds.crowdin.gamification.rest.builder;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import io.meeds.crowdin.gamification.model.RemoteProject;
import io.meeds.crowdin.gamification.model.WebHook;
import io.meeds.crowdin.gamification.rest.model.WebHookRestEntity;
import io.meeds.crowdin.gamification.storage.CrowdinConsumerStorage;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class WebHookBuilder {

  private static final Log LOG = ExoLogger.getLogger(WebHookBuilder.class);

  private WebHookBuilder() {
    // Class with static methods
  }

  public static WebHookRestEntity toRestEntity(CrowdinConsumerStorage crowdinConsumerStorage,
                                               WebHook webHook,
                                               boolean includeLanguages) {
    if (webHook == null) {
      return null;
    }
    RemoteProject remoteProject = null;
    try {
      remoteProject = crowdinConsumerStorage.retrieveRemoteProject(webHook.getProjectId(), includeLanguages, webHook.getToken());
    } catch (IllegalAccessException e) {
      LOG.error(e);
    }

    return new WebHookRestEntity(webHook.getId(),
                                 webHook.getWebhookId(),
                                 webHook.getProjectId(),
                                 webHook.getTriggers(),
                                 webHook.getEnabled(),
                                 webHook.getWatchedDate(),
                                 webHook.getWatchedBy(),
                                 webHook.getUpdatedDate(),
                                 webHook.getRefreshDate(),
                                 webHook.getProjectName(),
                                 remoteProject != null ? remoteProject.getIdentifier() : null,
                                 remoteProject != null ? remoteProject.getDescription() : null,
                                 remoteProject != null ? remoteProject.getAvatarUrl() : null,
                                 remoteProject != null ? remoteProject.getLanguages() : null,
                                 false,
                                 remoteProject != null);
  }

  public static List<WebHookRestEntity> toRestEntities(CrowdinConsumerStorage crowdinConsumerStorage,
                                                       Collection<WebHook> webHooks,
                                                       boolean includeLanguages) {
    return webHooks.stream().map(webHook -> toRestEntity(crowdinConsumerStorage, webHook, includeLanguages)).toList();
  }
}
