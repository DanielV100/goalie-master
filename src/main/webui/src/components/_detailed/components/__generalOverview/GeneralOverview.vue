<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import * as UtilityFunctions from "@/globals/utilityFunctions";
import * as SessionStorageFunctions from "@/globals/sessionStorageUtilitiyFunctions.js";
import * as GetRequestFunctions from "@/globals/getRequestUtilityFunctions.js";
import * as DeleteRequestFunctions from "@/globals/deleteRequestUtilityFunctions.js";

const isTrainingSession = ref(true);
const isExercise = ref(false);
const isGoalkeeper = ref(false);
//elements --> training session, exercises or goalkeeper (depends on nav bar)
const elements = ref();
onMounted(async () => {
  elements.value = await GetRequestFunctions.getAllTrainingSessions();
  let tmp = await GetRequestFunctions.getAllExercisesFromDB();
  tmp = await GetRequestFunctions.getGoalkeepersFromDB();
});


function trainingSessionsClicked() {
  isTrainingSession.value = true;
  isExercise.value = false;
  isGoalkeeper.value = false;
  elements.value = SessionStorageFunctions.getAllTrainingSessionFromSessionStorage();
}
function deleteTrainingSession(id) {
  DeleteRequestFunctions.deleteEntityById('training-session', id);
}

function exercisesClicked() {
  isTrainingSession.value = false;
  isExercise.value = true;
  isGoalkeeper.value = false;
  elements.value = SessionStorageFunctions.getAllExercisesFromSessionStorage();
}
function deleteExercise(id) {
  DeleteRequestFunctions.deleteEntityById('exercise', id);
}

function goalkeeperClicked() {
  isExercise.value = false;
  isTrainingSession.value = false;
  isGoalkeeper.value = true;
  elements.value = SessionStorageFunctions.getAllGoalkeepersFromSessionStorage();
}

function deleteGoalkeeper(id) {
  DeleteRequestFunctions.deleteEntityById('goalkeeper', id);
}

function deleteItemClicked(event) {
  if (confirm('Are you sure?')) {
    const idElement = event.target.parentElement.parentElement.firstChild;
    const id = idElement.innerText;
    if(isTrainingSession.value === true) {

    }
    if(isExercise.value === true) {

    }
    if(isGoalkeeper.value === true) {
      deleteGoalkeeper(id);
    }
  }
}

</script>

<template>
  <div class="__general_overview">
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
          <th></th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="element in elements">
          <td>{{ element.id }}</td>
          <td v-if="isTrainingSession || isExercise">{{ element.title }}</td>
          <td v-if="isTrainingSession">{{ element.tDate }}</td>
          <td v-if="isExercise">{{ element.duration }}</td>
          <td v-if="isGoalkeeper">{{ element.firstname }}</td>
          <td v-if="isGoalkeeper">{{ element.firstname }}</td>
          <td class="edit"><i class='bx bxs-edit-alt'></i></td>
          <td class="edit delete"><i @click="deleteItemClicked" class='bx bxs-trash-alt' ></i></td>
        </tr>
        </tbody>
    </table>
  </div>
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