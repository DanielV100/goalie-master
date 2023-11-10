
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
import {computed, onMounted, ref} from "vue";
import axios from "axios";
import * as UtilityFunctions from "@/globals/utilityFunctions";
import * as SessionStorageFunctions from "@/globals/sessionStorageUtilitiyFunctions.js";
const title = ref('');
const date = ref('');
const note = ref('');
const goalkeepersNameAndId = ref([]);

onMounted(async () => {
  let goalkeepersFromDB = await getGoalkeepersFromDB();
  createGoalkeepersSelectable(goalkeepersFromDB);

  let exercises = await getAllExercisesFromDB();
  SessionStorageFunctions.saveAllExercisesInSessionStorage(exercises);
});

const isSubmitDisabled = computed(() => {
  return !(title.value !== '' && date.value !== '');
});

const isCreatingTrainingSessionSuccessful = async () => {
  try {
    const trainingSession = {
      tTitle: title.value,
      tDate: date.value,
      tNotes: note.value,
      goalkeeperIds: getGoalkeeperIds(goalkeepers),
      exerciseIds: notes.value
    };
    await axios.post('/training_session/create', trainingSession, {
      headers: {
        Authorization: `Bearer ${UtilityFunctions.getJwtTokenFromSessionStorage()}`
      }
    });
    return true;
  } catch (error) {
    errorMessage.value = UtilityFunctions.errorHandling(error, LocalConfig.BUTTON_ADD_GOALKEEPER);
    resetForm();
    return false;
  }
}
async function onSubmitClick() {
  console.log(test.value);
  UtilityFunctions.setLoadingCircleInSubmitButton();
  //const isSuccessful = await isCreatingTrainingSessionSuccessful();
  if(isSuccessful) {
    isNotSubmitted.value = false;
  }
}

/**
 * The multiselect component needs an object of arrays as prop, which gets build in this method.
 * @param goalkeepers
 */
function createGoalkeepersSelectable(goalkeepers) {
  goalkeepers.forEach((goalkeeper) => {
    goalkeepersNameAndId.value.push({name:goalkeeper.firstname + ' ' + goalkeeper.lastname, id:goalkeeper.id});
  })
}

function getGoalkeeperIds(goalkeepers) {
  let goalkeeperIds = [];
  goalkeepers.forEach((goalkeeper) => {
    goalkeeperIds.push(goalkeeper.id);
  })
  return goalkeeperIds;
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

function optionSelected(value, id) {
  console.log(value.label);
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
                v-model="date"
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
          <Multiselect @select="optionSelected" :multiselect-options="goalkeepersNameAndId"/>
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
      <button class="submit" @click.prevent="onSubmitClick" :disabled="isSubmitDisabled">{{ LocalConfig.BUTTON_CREATE_TRAINING_SESSION }}</button>
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