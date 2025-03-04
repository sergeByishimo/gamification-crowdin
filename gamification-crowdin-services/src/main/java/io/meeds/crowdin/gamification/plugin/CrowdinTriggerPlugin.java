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

import org.exoplatform.container.component.BaseComponentPlugin;

import io.meeds.crowdin.gamification.model.Event;

import java.util.List;
import java.util.Map;

public abstract class CrowdinTriggerPlugin extends BaseComponentPlugin {

  /**
   * Gets List of triggered events
   *
   * @param trigger trigger event name
   * @param payload payload The raw payload of the webhook request.
   * @return List of triggered events
   */
  public abstract List<Event> getEvents(String trigger, Map<String, Object> payload);

  public abstract String getEventName();

  public abstract String getCancellingEventName();

  public abstract String getProjectId(Map<String, Object> payload);
}
