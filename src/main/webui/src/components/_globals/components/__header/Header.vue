<script setup>
/**
 * SFC which is a header on top of every page.
 */

/**** IMPORTS ****/
/**** CONFIGS ****/
import * as GlobalConfig from '@/globals/gloablConfig.js';
import * as SessionStorageFunctions from '@/globals/sessionStorageUtilitiyFunctions.js';

/**** CLICK-HANDLERS ****/
function logOutClicked() {
  SessionStorageFunctions.clearSessionStorage();
  GlobalConfig.changeWindowToTargetRoute('LoginForm');
}

function logoClicked() {
  GlobalConfig.changeWindowToTargetRoute('Overview');
}
function userIconClicked() {
}

</script>

<template>
  <div class="__header">
    <a @click="logoClicked" class="logo">{{ GlobalConfig.APP_NAME }}</a>
    <div class="header-right">
      <i @click="userIconClicked" class='bx bx-user'></i>
      <i @click="logOutClicked" class='bx bx-log-out'></i>
    </div>
  </div>
</template>

<style scoped>
.__header {
  overflow: hidden;
  background-color: var(--lightGrey);
}
.__header a {
  float: left;
  color: black;
  text-align: center;
  text-decoration: none;
}
.__header a.logo {
  padding: 12pt;
  font-size: 25pt;
  font-weight: bold;
}
.__header a:hover {
  background-color: var(--lightestGrey);
  color: black;
}
i {
  padding: 12pt;
  font-size: 35pt;
}
i:hover {
  font-weight: 900;
  cursor: pointer;
  background-color: var(--lightestGrey);
}
.header-right {
  float: right;
}
@media screen and (max-width: 500px) {
  .__header a {
    float: none;
    display: block;
    text-align: left;
  }
  .header-right {
    float: none;
  }
}
</style>