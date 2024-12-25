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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
export default {
  name: 'crowdin',
  title: 'crowdinConnector.label.profile',
  description: 'crowdinConnector.label.description',
  image: '/gamification-crowdin/images/crowdin.png',
  initialized: true,
  identifier: '',
  user: '',
  rank: 40,
  PROFILE_BASER_URL: 'https://crowdin.com/profile',
  init: () => {
    const lang = window.eXo?.env?.portal?.language || 'en';
    const url = `/gamification-crowdin/i18n/locale.portlet.CrowdinWebHookManagement?lang=${lang}`;
    return exoi18n.loadLanguageAsync(lang, url);
  },
  openOauthPopup(connector) {
    const width = 500;
    const height = 600;
    const left = window.innerWidth / 2 - width / 2;
    const top = window.innerHeight / 2 - height / 2;
    const authUrl = `https://accounts.crowdin.com/oauth/authorize?client_id=${connector.apiKey}&redirect_uri=${connector.redirectUrl}&response_type=code&scope=notification`;
    return window.open(authUrl, 'Crowdin OAuth', `width=${width}, height=${height}, left=${left}, top=${top}`);
  },
};
