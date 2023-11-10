<script setup>
import EditExerciseForm from "@/components/_detailed/components/__addExerciseForm/ViewExerciseForm.vue";
import {nextTick, onBeforeUpdate, onMounted, ref} from "vue";
import ViewExerciseForm from "@/components/_detailed/components/__addExerciseForm/ViewExerciseForm.vue";
import * as UtilityFunctions from '../../../../globals/utilityFunctions.js';
//ID's from the exercises to add
const exerciseIDs = ref([]);
const exerciseKeyfacts = ref([]);
const props = defineProps({
  category: String
});
let exercises = ref([]);
const exercisesKey = ref();
const totalDuration = ref('00:00');
const totalIntensity = ref(0);

const testKey = ref();


function addExerciseButtonClicked(event) {
  getExercisesFromSessionStorage();
  document.getElementById("myModal").style.display = "block";
}

function getExercisesFromSessionStorage() {
  exercises.value = [];
  testKey.value = generateRandomExerciseKey(testKey.value);
  exercises.value = UtilityFunctions.getExercisesFromSessionStorage(props.category);
  console.log(exercises.value);
}

function exerciseAccordionClicked(event) {
  toggleExerciseAccordion(event);
}

/**
 * Through toggling the exercises the website is better readable and has a better structure.
 * @param event
 */
function toggleExerciseAccordion(event) {
  const exerciseView = event.target.nextSibling;
  if(exerciseView.style.display === '') {
    exerciseView.style.overflow = 'hidden';
    exerciseView.style.display = 'none';
  } else {
    exerciseView.style.overflow = '';
    exerciseView.style.display = '';
  }
}

function modalBoxButtonClicked() {
 getIDsFromCheckedExercises();
}

/**
 * Method for getting ID's from checked exercises (which user chose in modal box).
 * exercisesKey is a trick to rerender the v-for. If it's not used, v-for won't update the way it should be.
 */
function getIDsFromCheckedExercises() {
  resetAccordion();
  let randomExerciseKey = generateRandomExerciseKey(exercisesKey.value);
  let checkedExercises = [];
  let numberOfChecked = 0;
  const modalCheckboxes = document.getElementsByClassName('modalCheckbox');
  for(let modalCheckbox of modalCheckboxes) {
    const idTableRow = modalCheckbox.parentElement.nextSibling;
    const titleTableRow = idTableRow.nextSibling;
    const durationTableRow = titleTableRow.nextSibling.nextSibling;
    const intensityTableRow = durationTableRow.nextSibling.nextSibling;
    if(modalCheckbox.checked === true) {
      numberOfChecked++;
      exercisesKey.value = randomExerciseKey;
      totalDuration.value = convertNumberToTime(convertTimeToNumber(totalDuration.value)+convertTimeToNumber(durationTableRow.textContent));
      totalIntensity.value += Number(intensityTableRow.textContent);
      checkedExercises.push({exerciseID: idTableRow.textContent, exerciseTitle: titleTableRow.textContent});
    }
  }
  calculateTotalIntensity(numberOfChecked);
  exerciseKeyfacts.value = checkedExercises;
  document.getElementById("myModal").style.display = "none";
}

function calculateTotalIntensity(numberOfCheckedExercises) {
  totalIntensity.value = Math.round(totalIntensity.value/numberOfCheckedExercises);
}

function resetAccordion() {
  totalDuration.value = '00:00';
  totalIntensity.value = 0;
}



/**
 * Needed to make unique ids for the v-for (forcing re-rendering).
 * @returns {number}
 */
function generateRandomExerciseKey(exerciseKey) {
  let randomExerciseKey = Math.floor(Math.random() * Number.MAX_SAFE_INTEGER);
  if(exerciseKey === randomExerciseKey) {
    return randomExerciseKey-1;
  } else {
    return randomExerciseKey;
  }
}

function convertTimeToNumber(timeString) {
  const [hours, minutes] = timeString.split(':').map(Number);
  return hours * 60 + minutes;
}
function convertNumberToTime(totalMinutes) {
  const hours = Math.floor(totalMinutes / 60);
  const minutes = totalMinutes % 60;
  const formattedHours = String(hours).padStart(2, '0'); // Ensure two digits for hours
  const formattedMinutes = String(minutes).padStart(2, '0'); // Ensure two digits for minutes
  return `${formattedHours}:${formattedMinutes}`;
}

</script>

<template>
  <div class="__accordion">
    <div class="table-container">
      <table>
        <tr>
          <td style="width: 50%">
            <label>Kategorie</label>
            <h2><slot></slot></h2>
          </td>
          <td class="after_td_one">
            <label>Dauer</label>
            <h2>{{ totalDuration }}</h2>
          </td>
          <td class="after_td_one">
            <label>Intensität</label>
            <h2>{{ totalIntensity }} /5</h2>
          </td>
        </tr>
      </table>
      <div v-for="exercise in exerciseKeyfacts" :key="exercisesKey" id="testArea">
        <br>
        <div @click="exerciseAccordionClicked" class="exercise_accordion">
          {{ exercise.exerciseTitle}}
          <i class='bx bx-chevron-right'></i>
        </div>
        <ViewExerciseForm style="overflow: hidden; display: none" :exercise-i-d="Number(exercise.exerciseID)" :category="props.category"></ViewExerciseForm>
      </div>
      <br>
      <button :id="props.category" @click="addExerciseButtonClicked">+</button>

      <div class="modal" id="myModal">
        <div class="modal-dialog">
          <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
              <h4 class="modal-title">Übungen auswählen</h4>
              <button type="button" class="close" id="closeModal">&times;</button>
            </div>

            <!-- Modal body -->
            <div class="modal-body">
              <div class="table-container">
                <table class="table table-striped">
                  <thead>
                  <tr>
                    <th>Im Training</th>
                    <th>ID</th>
                    <th>Titel</th>
                    <th>Kategorie</th>
                    <th>Dauer</th>
                    <th>Anzahl TH</th>
                    <th>Intensität</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="exercise in exercises">
                    <td>
                      <input type="checkbox" class="modalCheckbox"/>
                    </td>
                    <td>{{ exercise.id }}</td>
                    <td>{{ exercise.title }}</td>
                    <td>{{ exercise.category }}</td>
                    <td>{{ exercise.duration }}</td>
                    <td>{{ exercise.numberOfGoalkeepers }}</td>
                    <td>{{ exercise.intensity }}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>


            <!-- Modal footer -->
            <div class="modal-footer">
              <button @click="modalBoxButtonClicked" type="button" class="btn btn-danger" id="closeModal">Close</button>
            </div>

          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<style scoped>
/* Style the buttons that are used to open and close the accordion panel */
.exercise_accordion {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: var(--lightestGrey);
  color: black;
  cursor: pointer;
  padding: 18px;
  width: 100%;
  text-align: left;
  border: none;
  outline: none;
  transition: 0.4s;
}

.exercise_accordion i {
  font-size: 1.5rem; /* Adjust the size of the icon */
}

/* Add a background color to the button if it is clicked on (add the .active class with JS), and when you move the mouse over it (hover) */
.exercise_accordion:hover {
  background-color: #ccc;
}

.__accordion {
  width: 100%;
}

.table-container table {
  width: 100%;
  border-collapse: collapse;
}
.after_td_one {
  padding-left: 20px;
}
.after_td_one h2 {
  color: var(--placeholderColor);
}
h2 {
  margin-top: 0;
}
button {
  width: 100%;
}

.modal {
  color: black;
  display: none;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgb(0, 0, 0);
  background-color: rgba(0, 0, 0, 0.4);
}

.modal-dialog {
  margin: 60px auto;
  width: 80%;
}

.modal-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-title {
  flex: 1; /* Takes up remaining space */
  margin: 0; /* Remove any default margin */
}

.close {
  margin-left: 10px; /* Adjust as needed */
}


.close {
  flex: 1;
  font-size: 30px;
  font-weight: bold;
  color: #000;
  border: none;
  background: none;
  cursor: pointer;
}

.close:focus {
  outline: none;
}

.modal-body {
  margin-bottom: 20px;
}

.table {
  width: 100%;
  border-collapse: collapse;
}

.table th, .table td {
  padding: 8px 12px;
  border: 1px solid #ddd;
}

.table th {
  background-color: #f2f2f2;
  font-weight: bold;
}

.table tbody tr:nth-child(even) {
  background-color: #f9f9f9;
}

.table tbody tr:hover {
  background-color: #f1f1f1;
}

.modal-footer {
  text-align: right;
}

.table-container {
  width: 100%;
  overflow-x: auto;
}

.table {
  width: max-content;
  border-collapse: collapse;
}

.table th, .table td {
  padding: 8px 12px;
  border: 1px solid #ddd;
}

.table th {
  background-color: #f2f2f2;
  font-weight: bold;
}

.table tbody tr:nth-child(even) {
  background-color: #f9f9f9;
}

.table tbody tr:hover {
  background-color: #f1f1f1;
}

</style>
