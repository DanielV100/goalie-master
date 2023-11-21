<script setup>
/**
 * SFC which shows all entities in tables and make them deletable, editable, shareable, downloadable.
 * @author daniel
 */

/**** IMPORTS ****/
import {onMounted, ref} from "vue";
/**** UTILITY FUNCTIONS ****/
import * as SessionStorageFunctions from "@/globals/sessionStorageUtilitiyFunctions.js";
import * as GetRequestFunctions from "@/globals/getRequestUtilityFunctions.js";
import * as DeleteRequestFunctions from "@/globals/deleteRequestUtilityFunctions.js";
/**** COMPONENTS ****/
import EntityEditor from "@/components/_detailed/components/__entityEditor/EntityEditor.vue";
import {all} from "axios";

/**** VARIABLES ****/
const isTrainingSession = ref(true);
const isExercise = ref(false);
const isGoalkeeper = ref(false);
//elements --> training session, exercises or goalkeeper (depends on nav bar)
const elements = ref();
const entityId = ref(26);
const entityType = ref();
const isShowingOverview = ref(true);

const isPastDate = (dateStr) => {
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  const elementDate = new Date(dateStr);
  return elementDate < today;
};
/**** HOOKS ****/
onMounted( () => {
  init();
});

/**** CLICK-HANDLERS ****/

/**
 * Triggered when training session is clicked
 */
function trainingSessionsClicked() {
  isTrainingSession.value = true;
  isExercise.value = false;
  isGoalkeeper.value = false;
  elements.value = SessionStorageFunctions.getAllTrainingSessionFromSessionStorage();
}

/**
 * Triggered when exercises session is clicked
 */
function exercisesClicked() {
  isTrainingSession.value = false;
  isExercise.value = true;
  isGoalkeeper.value = false;
  elements.value = SessionStorageFunctions.getAllExercisesFromSessionStorage();
}

/**
 * Triggered when goalkeeper session is clicked
 */
function goalkeeperClicked() {
  isExercise.value = false;
  isTrainingSession.value = false;
  isGoalkeeper.value = true;
  elements.value = SessionStorageFunctions.getAllGoalkeepersFromSessionStorage();
}

/**
 * Triggered when delete icon is clicked
 * @param event click event
 */
async function deleteItemClicked(event) {
  if (confirm('Sind Sie sicher?')) {
    const id = getIdFromClickedElement(event);
    if(isTrainingSession.value === true) {
      alert(await deleteTrainingSession(id));
      await refreshAfterTrainingSessionDeleted();
    }
    if(isExercise.value === true) {
      alert(await deleteExercise(id));
      await refreshAfterExerciseDeleted();

    }
    if(isGoalkeeper.value === true) {
      alert(await deleteGoalkeeper(id));
      await refreshAfterGoalkeeperDeleted();
    }
  }
}

/**
 * Triggered when update item clicked
 * @param event click event
 */
function updateItemClicked(event) {
  const id = getIdFromClickedElement(event);
  if(isGoalkeeper.value === true) {
    entityType.value = 'goalkeeper';
  }
  if(isExercise.value === true) {
    entityType.value = 'exercise';
  }
  if(isTrainingSession.value === true) {
    entityType.value = 'training-session';
  }
  entityId.value = id;
  isShowingOverview.value = false;
}

/**
 * Download icon clicked
 * @param event
 */
function downloadIconClicked(event) {
  downloadPdf(getIdFromClickedElement(event));
}

/**
 * Share icon clicked
 * @param event
 */
function shareIconClicked(event) {
  const mail = prompt("E-Mail-Adresse eingeben");
  if(isValidEmail(mail)) {
    sendEmail(getIdFromClickedElement(event), mail);
  } else {
    alert("Bitte geben Sie eine gültige E-Mail-Adresse ein.");
  }
}

/**** FUNCTIONS ****/
/**
 * Loading entities from db and save it to session storage.
 * @important session storage can only save 5mb!
 * @returns {Promise<void>}
 */
async function init() {
  elements.value = await GetRequestFunctions.getAllTrainingSessions();
  let tmp = await GetRequestFunctions.getAllExercisesFromDB();
  console.log(tmp);
  let tmp2 = await GetRequestFunctions.getGoalkeepersFromDB();
}

/**
 * Checks if given mail is a valid mail address via regex
 * @param mail
 * @returns {boolean}
 */
function isValidEmail(mail) {
  const regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
  return regex.test(mail);
}

/**** DELETE HANDLERS ****/
async function deleteTrainingSession(id) {
  return await DeleteRequestFunctions.deleteEntityById('training_session', id);
}
async function deleteExercise(id) {
  return await DeleteRequestFunctions.deleteEntityById('exercise', id);
}
async function deleteGoalkeeper(id) {
  return await DeleteRequestFunctions.deleteEntityById('goalkeeper', id);
}

/**** REFRESH HANDLERS ****/
async function refreshAfterTrainingSessionDeleted() {
  elements.value = await GetRequestFunctions.getAllTrainingSessions();
}
async function refreshAfterExerciseDeleted() {
  elements.value = await GetRequestFunctions.getAllExercisesFromDB();
}
async function refreshAfterGoalkeeperDeleted() {
  elements.value = await GetRequestFunctions.getGoalkeepersFromDB();
}


/**
 * Gets entity id from click event in table.
 * @param event click event
 * @returns id as string
 */
function getIdFromClickedElement(event) {
  const idElement = event.target.parentElement.parentElement.firstChild;
  return idElement.innerText;
}

/**
 * Download pdf (create pseudo element, so that download starts automatically)
 * @param id entity id
 * @returns {Promise<void>}
 */
async function downloadPdf(id) {
  const blob = await GetRequestFunctions.getTrainingSessionAsPdf(id);
  const link = document.createElement('a');
  link.href = URL.createObjectURL(blob);
  link.download = blob.name;
  link.click();
  URL.revokeObjectURL(link.href);
}

/**
 * Method for triggering the http-request for getting pdf as mail.
 * @param id
 * @param mail
 * @returns {Promise<void>}
 */
async function sendEmail(id, mail) {
  try {
    alert(await GetRequestFunctions.getTrainingSessionAsMail(id, mail));
  } catch (error) {
    console.error('Fehler beim Senden der E-Mail:', error);
    alert('Ein unerwarteter Fehler ist aufgetreten.');
  }
}
</script>

<template>
  <div v-if="isShowingOverview" class="__general_overview">
    <div class="top_navigation_buttons">
      <button @click="trainingSessionsClicked">Trainings</button>
      <button @click="exercisesClicked">Übungen</button>
      <button @click="goalkeeperClicked">Torhüter</button>
    </div>
    <table class="general_overview_table">
        <thead>
        <tr>
          <th>ID</th>
          <th v-if="isTrainingSession || isExercise">Titel</th>
          <th v-if="isTrainingSession">Datum</th>
          <th v-if="isExercise">Dauer</th>
          <th v-if="isGoalkeeper">Vorname</th>
          <th v-if="isGoalkeeper">Nachname</th>
          <th v-if="isTrainingSession"></th>
          <th v-if="isTrainingSession"></th>
          <th></th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="element in elements" :class="{ 'past-date': isPastDate(element.date) }">
          <td>{{ element.id }}</td>
          <td v-if="isTrainingSession || isExercise">{{ element.title }}</td>
          <td v-if="isTrainingSession">{{ element.date }}</td>
          <td v-if="isExercise">{{ element.duration }}</td>
          <td v-if="isGoalkeeper">{{ element.firstname }}</td>
          <td v-if="isGoalkeeper">{{ element.lastname }}</td>
          <td class="edit" v-if="isTrainingSession"><i @click="downloadIconClicked" class='bx bxs-download'></i></td>
          <td class="edit" v-if="isTrainingSession"><i @click="shareIconClicked" class='bx bxs-share-alt'></i></td>
          <td class="edit"><i @click="updateItemClicked" class='bx bxs-edit-alt'></i></td>
          <td class="edit delete"><i @click="deleteItemClicked" class='bx bxs-trash-alt' ></i></td>
        </tr>
        </tbody>
    </table>
  </div>
  <EntityEditor v-else :entity-id="Number(entityId)" :entity-type="entityType" />
</template>

<style scoped>
.__general_overview {
  width: 100%;
}
.top_navigation_buttons {
  display: flex;
}

button {
  flex-grow: 1;
  padding: 20px;
  border-radius: 0;
}

.general_overview_table {
  text-align: left;
  width: 100%;
}

.edit {
  text-align: center;
  font-size: 20px;
}
.delete {
  color: red;
}
.past-date {
  background-color: var(--placeholderColor);
}
</style>