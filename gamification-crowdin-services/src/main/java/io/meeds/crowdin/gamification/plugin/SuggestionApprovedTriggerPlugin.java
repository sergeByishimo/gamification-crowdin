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
package io.meeds.crowdin.gamification.plugin;

import io.meeds.crowdin.gamification.model.Event;
import io.meeds.crowdin.gamification.model.RemoteApproval;
import io.meeds.crowdin.gamification.services.CrowdinTriggerService;
import io.meeds.crowdin.gamification.services.WebhookService;
import io.meeds.gamification.model.RealizationDTO;
import io.meeds.gamification.service.RealizationService;
import jakarta.annotation.PostConstruct;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.meeds.crowdin.gamification.utils.Utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SuggestionApprovedTriggerPlugin extends CrowdinTriggerPlugin {

  private static final Log LOG            = ExoLogger.getLogger(SuggestionApprovedTriggerPlugin.class);

  @Autowired
  private CrowdinTriggerService crowdinTriggerService;

  @Autowired
  private RealizationService    realizationService;

  @Autowired
  private WebhookService        webhookService;

  @PostConstruct
  public void init() {
    crowdinTriggerService.addPlugin(this);
  }

  @Override
  public List<Event> getEvents(String trigger, Map<String, Object> payload) {
    String objectId = constructObjectIdAsJsonString(payload, TRANSLATION);

    List<Event> eventList = new ArrayList<>();
    eventList.add(new Event(SUGGESTION_APPROVED_EVENT_NAME,
                            extractSubItem(payload, TRANSLATION, USER, USERNAME),
                            extractSubItem(payload, TRANSLATION, USER, USERNAME),
                            objectId,
                            TRANSLATION,
                            getProjectId(payload),
                            extractSubItem(payload, TRANSLATION, TARGET_LANGUAGE, ID),
                            extractSubItem(payload, TRANSLATION, PROVIDER) == null,
                            extractSubItem(payload, TRANSLATION, STRING, FILE, DIRECTORY_ID),
                            trigger.equals(SUGGESTION_DISAPPROVED_TRIGGER),
                            countWords(extractSubItem(payload, TRANSLATION, STRING, TEXT))));


    if (trigger.equals(SUGGESTION_APPROVED_TRIGGER)) {
      // Retrieve who made that approval by calling Crowdin API
      String translationId = extractSubItem(payload, TRANSLATION, ID);
      RemoteApproval remoteApproval = webhookService.getApproval(getProjectId(payload), translationId);

      if (remoteApproval != null) {
        eventList.add(new Event(APPROVE_SUGGESTION_EVENT_NAME,
                remoteApproval.getUserName(),
                remoteApproval.getUserName(),
                objectId,
                APPROVAL,
                getProjectId(payload),
                extractSubItem(payload, TRANSLATION, TARGET_LANGUAGE, ID),
                extractSubItem(payload, TRANSLATION, PROVIDER) == null,
                extractSubItem(payload, TRANSLATION, STRING, FILE, DIRECTORY_ID),
                false,
                countWords(extractSubItem(payload, TRANSLATION, STRING, TEXT))));
      }
    }

    if (trigger.equals(SUGGESTION_DISAPPROVED_TRIGGER)) {
      // Retrieve who made that approval from the database

      List<RealizationDTO> realizations = realizationService.
              findRealizationsByObjectIdAndObjectType(objectId, APPROVAL);

      if ( ! realizations.isEmpty()) {
        String approvalUsername = realizations.get(0).getEarnerId();

        eventList.add(new Event(APPROVE_SUGGESTION_EVENT_NAME,
                approvalUsername,
                approvalUsername,
                objectId,
                TRANSLATION,
                getProjectId(payload),
                extractSubItem(payload, TRANSLATION, TARGET_LANGUAGE, ID),
                extractSubItem(payload, TRANSLATION, PROVIDER) == null,
                extractSubItem(payload, TRANSLATION, STRING, FILE, DIRECTORY_ID),
                true,
                countWords(extractSubItem(payload, TRANSLATION, STRING, TEXT))));
      }

    }

    return eventList;
  }

  @Override
  public String getEventName() {
    return SUGGESTION_APPROVED_TRIGGER;
  }

  @Override
  public String getCancellingEventName() {
    return SUGGESTION_DISAPPROVED_TRIGGER;
  }

  @Override
  public String getProjectId(Map<String, Object> payload) {
    return extractSubItem(payload, TRANSLATION, STRING, PROJECT, ID);
  }
}
