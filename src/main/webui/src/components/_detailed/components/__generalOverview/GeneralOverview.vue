<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import * as UtilityFunctions from "@/globals/utilityFunctions";
import * as SessionStorageFunctions from "@/globals/sessionStorageUtilitiyFunctions.js";
import * as GetRequestFunctions from "@/globals/getRequestUtilityFunctions.js";
import * as DeleteRequestFunctions from "@/globals/deleteRequestUtilityFunctions.js";
import AddGoalkeeperForm from "@/components/_detailed/components/__addGoalkeeperForm/AddGoalkeeperForm.vue";
import EntityEditor from "@/components/_detailed/components/__entityEditor/EntityEditor.vue";

const isTrainingSession = ref(true);
const isExercise = ref(false);
const isGoalkeeper = ref(false);
//elements --> training session, exercises or goalkeeper (depends on nav bar)
const elements = ref();

const entityId = ref(26);
const entityType = ref('goalkeeper');

const isShowingOverview = ref(true);
onMounted( () => {
  init();
});

async function init() {
  elements.value = await GetRequestFunctions.getAllTrainingSessions();
  let tmp = await GetRequestFunctions.getAllExercisesFromDB();
  tmp = await GetRequestFunctions.getGoalkeepersFromDB();
}


function trainingSessionsClicked() {
  isTrainingSession.value = true;
  isExercise.value = false;
  isGoalkeeper.value = false;
  elements.value = SessionStorageFunctions.getAllTrainingSessionFromSessionStorage();
}
async function deleteTrainingSession(id) {
  await DeleteRequestFunctions.deleteEntityById('training_session', id);
}
async function refreshAfterTrainingSessionDeleted() {
  elements.value = await GetRequestFunctions.getAllExercisesFromDB();
}

function exercisesClicked() {
  isTrainingSession.value = false;
  isExercise.value = true;
  isGoalkeeper.value = false;
  elements.value = SessionStorageFunctions.getAllExercisesFromSessionStorage();
}
async function deleteExercise(id) {
  await DeleteRequestFunctions.deleteEntityById('exercise', id);
}
async function refreshAfterExerciseDeleted() {
  elements.value = await GetRequestFunctions.getAllExercisesFromDB();
}

function goalkeeperClicked() {
  isExercise.value = false;
  isTrainingSession.value = false;
  isGoalkeeper.value = true;
  elements.value = SessionStorageFunctions.getAllGoalkeepersFromSessionStorage();
}

async function deleteGoalkeeper(id) {
  await DeleteRequestFunctions.deleteEntityById('goalkeeper', id);
}
async function refreshAfterGoalkeeperDeleted() {
  elements.value = await GetRequestFunctions.getGoalkeepersFromDB();
}

async function deleteItemClicked(event) {
  if (confirm('Are you sure?')) {
    const id = getIdFromClickedElement(event);
    if(isTrainingSession.value === true) {
      await deleteTrainingSession(id);
      await refreshAfterTrainingSessionDeleted();
    }
    if(isExercise.value === true) {
      await deleteExercise(id);
      await refreshAfterExerciseDeleted();

    }
    if(isGoalkeeper.value === true) {
      await deleteGoalkeeper(id);
      await refreshAfterGoalkeeperDeleted();
    }
  }
}

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

function getIdFromClickedElement(event) {
  const idElement = event.target.parentElement.parentElement.firstChild;
  return idElement.innerText;
}

function downloadIconClicked(event) {
  downloadPdf(getIdFromClickedElement(event));
}

async function downloadPdf(id) {
  const response = await fetch('/training_session/download/' + id);
  const blob = await response.blob();
  const link = document.createElement('a');
  link.href = URL.createObjectURL(blob);
  link.download = 'trainingsession.pdf';
  link.click();
  URL.revokeObjectURL(link.href);
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
          <th></th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="element in elements">
          <td>{{ element.id }}</td>
          <td v-if="isTrainingSession || isExercise">{{ element.title }}</td>
          <td v-if="isTrainingSession">{{ element.date }}</td>
          <td v-if="isExercise">{{ element.duration }}</td>
          <td v-if="isGoalkeeper">{{ element.firstname }}</td>
          <td v-if="isGoalkeeper">{{ element.lastname }}</td>
          <td class="edit" v-if="isTrainingSession"><i @click="downloadIconClicked" class='bx bxs-download'></i></td>
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

</style>