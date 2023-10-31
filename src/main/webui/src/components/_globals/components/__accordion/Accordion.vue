<script setup>
import EditExerciseForm from "@/components/_detailed/components/__addExerciseForm/ViewExerciseForm.vue";
import {onMounted, ref} from "vue";
import ViewExerciseForm from "@/components/_detailed/components/__addExerciseForm/ViewExerciseForm.vue";
//ID's from the exercises to add
const exerciseIDs = ref([]);
const exerciseKeyfacts = ref([]);


function addExerciseButtonClicked() {
  exerciseIDs.value.push(prompt('Inout'));

  exerciseKeyfacts.value = [
    {exerciseID: '1', exerciseName: 'Hechten II'},
    {exerciseID: '2', exerciseName: 'Hechten III'},
    {exerciseID: '3', exerciseName: 'Koordination'},
  ]
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
            <h2>00:00</h2>
          </td>
          <td class="after_td_one">
            <label>Intensität</label>
            <h2>2/5</h2>
          </td>
        </tr>
      </table>
      <div v-for="(exercise, index) in exerciseKeyfacts" :key="index" id="testArea">
        <br>
        <div @click="exerciseAccordionClicked" class="exercise_accordion">
          {{ exercise.exerciseName}}
          <i class='bx bx-chevron-right'></i>
        </div>
        <ViewExerciseForm style="overflow: hidden; display: none" :exercise-i-d="exercise.exerciseID"></ViewExerciseForm>
      </div>
      <br>
      <button @click="addExerciseButtonClicked">+</button>
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

.table-container {
  overflow-x: auto;
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


</style>
