<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import * as UtilityFunctions from "@/globals/utilityFunctions";
import * as SessionStorageFunctions from "@/globals/sessionStorageUtilitiyFunctions.js";
import * as GetRequestFunctions from "@/globals/getRequestUtilityFunctions.js";

const isTrainingSession = ref(true);
const isExercise = ref(false);
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
  elements.value = SessionStorageFunctions.getAllTrainingSessionFromSessionStorage();
}

function exercisesClicked() {
  isTrainingSession.value = false;
  isExercise.value = true;
  elements.value = SessionStorageFunctions.getAllExercisesFromSessionStorage();
}

function goalkeeperClicked() {
  isExercise.value = false;
  isTrainingSession.value = false;
  elements.value = SessionStorageFunctions.getAllGoalkeepersFromSessionStorage();
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
          <th>Titel</th>
          <th v-if="isTrainingSession">Datum</th>
          <th v-if="isExercise">Dauer</th>
          <th></th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="element in elements">
          <td>{{ element.id }}</td>
          <td>{{ element.title }}</td>
          <td v-if="isTrainingSession">{{ element.tDate }}</td>
          <td v-if="isExercise">{{ element.duration }}</td>
          <td class="edit"><i class='bx bxs-edit-alt'></i></td>
          <td class="edit delete"><i class='bx bxs-trash-alt' ></i></td>
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