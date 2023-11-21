<script setup>
/**
 * SFC which is a header on top of every page.
 */

/**** IMPORTS ****/
/**** CONFIGS ****/
import * as GlobalConfig from '@/globals/gloablConfig.js';
import * as SessionStorageFunctions from '@/globals/sessionStorageUtilitiyFunctions.js';
import axios from "axios";
import * as UtilityFunctions from "@/globals/utilityFunctions";
import * as LocalConfig from "@/components/_detailed/components/__goalkeeperForm/resources/addGoalkeeperFormConfig";

/**** HTTP-REQUESTS ****/
const isUpdatingPasswordSuccessful = async (newPassword) => {
  try {
    await axios.put('/login/update', newPassword, {
      headers: {
        'Content-Type':'text/plain',
        Authorization: `Bearer ${SessionStorageFunctions.getJwtTokenFromSessionStorage()}`
      }
    });
    return 'Passwort aktualisiert!';
  } catch (error) {
    return 'Fehler aufgetretetn - Passwort muss mindestes sechs Zeichen lang sein!' + error;
  }
}
/**** CLICK-HANDLERS ****/
function logOutClicked() {
  SessionStorageFunctions.clearSessionStorage();
  GlobalConfig.changeWindowToTargetRoute('LoginForm');
}

function logoClicked() {
  GlobalConfig.changeWindowToTargetRoute('Overview');
}

/**
 * User can change it's password by clicking on user icon.
 */
async function userIconClicked() {
  const newPassword = prompt("Neues Passwort eingeben");
  if(newPassword.length > 6) {
    alert(await isUpdatingPasswordSuccessful(newPassword));
  } else {
    alert("Passwort muss mindestens 6 Zeichen enthalten!");
  }
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