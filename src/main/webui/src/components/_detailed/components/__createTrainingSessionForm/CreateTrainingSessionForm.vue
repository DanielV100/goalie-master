
<script setup>
/**
 * @Important: Calling the accordions is only working correctly with passing a modalID.
 * Otherwise, the states of the three accordions appear interfering and some stupid things happen.
 * @TimeToFindOut: 120min.
 */

import CurrentPageIndicator from "@/components/_globals/components/__currentPageIndicator/CurrentPageIndicator.vue";
import * as LocalConfig from './resources/createTrainingSessionForm.js';
import Multiselect from "@/components/_globals/components/__multiselect/Multiselect.vue";
import Accordion from "@/components/_globals/components/__accordion/Accordion.vue";
import {onMounted, ref} from "vue";
import axios from "axios";
import * as UtilityFunctions from "@/globals/utilityFunctions";
import * as SessionStorageFunctions from "@/globals/sessionStorageUtilitiyFunctions.js";

let goalkeepersFromDB = ref([]);
onMounted(async () => {
  let goalkeepers = await getGoalkeepersFromDB();
  createGoalkeepersSelectable(goalkeepers);

  let exercises = await getAllExercisesFromDB();
  SessionStorageFunctions.saveAllExercisesInSessionStorage(exercises);
});

/**
 * The multiselect component needs an object of arrays as prop, which gets build in this method.
 * @param goalkeepers
 */
function createGoalkeepersSelectable(goalkeepers) {
  goalkeepers.forEach((goalkeeper) => {
    goalkeepersFromDB.value.push({name:goalkeeper.firstname + ' ' + goalkeeper.lastname, id:goalkeeper.id});
  })
}

/**
 * Method makes http-request and gets goalkeepers from db.
 * @returns {Promise<any|*[]>}
 */
async function getGoalkeepersFromDB() {
  try {
    const response = await axios.get('goalkeeper/get/specific', {
      headers: {
        accept: 'application/json',
        Authorization: `Bearer ${UtilityFunctions.getJwtTokenFromSessionStorage()}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(error);
    return [];
  }
}

async function getAllExercisesFromDB() {
  try {
    const response = await axios.get('exercise/get/specific', {
      headers: {
        accept: 'application/json',
        Authorization: `Bearer ${UtilityFunctions.getJwtTokenFromSessionStorage()}`
      }
    });
    console.log(response);
    return response.data;
  } catch (error) {
    console.error(error);
    return [];
  }
}
</script>

<template>
  <div class="__create_training_session_form">
    <CurrentPageIndicator>{{ LocalConfig.CURRENT_PAGE }}</CurrentPageIndicator>
    <div class="wrapper">
      <form @submit="onSubmitClick" class="form">
        <div class="column">
          <div class="input-box">
            <label>{{ LocalConfig.FORM_TITLE_LABEL }}</label>
            <input
                v-model="title"
                type="text"
                placeholder="SVS - 1-g-1"
                required
            />
          </div>
          <div class="input-box">
            <label>{{ LocalConfig.FORM_DATE_LABEL }}</label>
            <input
                v-model="title"
                id="firstname"
                type="text"
                placeholder="Hechten II"
                required
            />
          </div>
        </div>
        <br>
        <div>
          <label>{{ LocalConfig.FORM_CHOOSE_GOALKEEPER_LABEL }}</label>
          <Multiselect :multiselect-options="goalkeepersFromDB"/>
        </div>
      </form>
      <br>
      <Accordion category="warmUp" modal-i-d="_warmup">Aufwärmen</Accordion>
      <br>
      <br>
      <Accordion category="main" modal-i-d="_main">Hauptteil</Accordion>
      <br>
      <br>
      <Accordion category="end" modal-i-d="_end">Abschluss</Accordion>
      <br>
      <div>
        <br>
        <label>Notizen</label>
        <textarea class="input" v-model="note" id="textTest" rows="3"></textarea>
      </div>
    </div>
  </div>
</template>

<style scoped>
.__create_training_session_form {
  display: grid;
}
.wrapper {
  min-width: 80%;
  max-width: 80%;
  background: var(--lightGrey);
  text-align: left;
}
.form :where(.input-box input) {
  position: relative;
  height: 50px;
  width: 100%;
  margin-top: 8px;
}

#soccerFieldContainer {
  display: grid;
  cursor: crosshair;
  padding: 0;
}

#multi button {
  margin-top: 10pt;
}

button {
  width: 100%;
}
</style>