<script setup>
import CurrentPageIndicator from "@/components/_globals/components/__currentPageIndicator/CurrentPageIndicator.vue";
import * as LocalConfig from './resources/createTrainingSessionForm.js';
import Multiselect from "@/components/_globals/components/__multiselect/Multiselect.vue";
import Accordion from "@/components/_globals/components/__accordion/Accordion.vue";
import {onMounted, ref} from "vue";
import axios from "axios";
import * as UtilityFunctions from "@/globals/utilityFunctions";

let goalkeepersFromDB = ref([]);
onMounted(async () => {
  let goalkeepers = await getGoalkeepersFromDB();
  createGoalkeepersSelectable(goalkeepers);

  let exercises = await getAllExercisesFromDB();
  saveAllExercisesInSessionStorage(exercises);
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

/**
 * Filters exercises by category group and saves them in session storage.
 * Saving in session storage is faster than requesting db.
 * @param exercises
 */
function saveAllExercisesInSessionStorage(exercises) {
  let exerciseWarmUp = [];
  let exerciseMain = [];
  let exerciseEnd = [];
  let exerciseOther = [];
  exercises.forEach((exercise) => {
    if(exercise.categoryGroup.toLowerCase() === 'aufwärmen') {
      exerciseWarmUp.push(exercise);
    } else if(exercise.categoryGroup.toLowerCase() === 'hauptteil') {
      exerciseMain.push(exercise);
    } else if(exercise.categoryGroup.toLowerCase() === 'schluss') {
      exerciseEnd.push(exercise);
    } else {
      exerciseOther.push(exercise);
    }
  });
  sessionStorage.setItem('exercisesWarmUp', JSON.stringify(exerciseWarmUp));
  sessionStorage.setItem('exercisesMain', JSON.stringify(exerciseMain));
  sessionStorage.setItem('exercisesEnd', JSON.stringify(exerciseEnd));
  sessionStorage.setItem('exercisesOther', JSON.stringify(exerciseOther));
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
      <Accordion category="warmUp">Aufwärmen</Accordion>
      <br>
      <br>
      <Accordion category="main">Hauptteil</Accordion>
      <br>
      <br>
      <Accordion category="end">Abschluss</Accordion>
      <br>
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