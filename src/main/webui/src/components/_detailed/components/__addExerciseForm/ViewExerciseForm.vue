<script setup>
import * as LocalConfig from './resources/addExerciseFormConfig.js';
import * as UtilityFunctions from '../../../../globals/utilityFunctions.js';
import CurrentPageIndicator from "@/components/_globals/components/__currentPageIndicator/CurrentPageIndicator.vue";
import SoccerField from "@/components/_globals/components/__soccerField/SoccerField.vue";
import {computed, onBeforeMount, onMounted, ref} from "vue";
import SuccessAnimation from "@/components/_globals/components/__successAnimation/SuccessAnimation.vue";

const title = ref('');
const category = ref('');
const numberOfGoalkeeper = ref('');
const duration = ref('');
const intensity = ref('');
const materialList = ref([]);
const descriptionList = ref([]);
const sketchDataUrl = ref();
const note = ref();
const isNoteNotEmpty = ref(false);

let exerciseID;
const props = defineProps({
  category: String,
  exerciseID: Number
});

onMounted(() => {
  exerciseID = props.exerciseID;
  console.log(exerciseID);
  fillFormFields();
});

/**
 * This function fills the form with the data from the database.
 */
function fillFormFields() {
  let exercises = JSON.parse(sessionStorage.getItem('exercisesWarmUp'));
  exercises.forEach((exercise)=> {
    if(exercise.id === exerciseID) {
      let test = [];
      for(let i = 0; i < exercise.numbersOfMaterial.length; i++) {
        test.push({numberOfMaterial: exercise.numbersOfMaterial[i], material: exercise.materials[i]});
      }
      title.value = exercise.title;
      category.value = exercise.category;
      numberOfGoalkeeper.value = exercise.numberOfGoalkeepers;
      duration.value = exercise.duration;
      intensity.value = exercise.intensity;
      descriptionList.value = exercise.descriptionSteps;
      materialList.value = test;
      if (exercise.note === null || exercise.note === '') {
        isNoteNotEmpty.value = false;
      } else {
        isNoteNotEmpty.value = true;
        note.value = exercise.note;
      }
      byteArrayToDataURL(exercise.sketch)
    }
  });
}
function byteArrayToDataURL(byteArray) {
  let blob = new Blob([new Uint8Array(byteArray)], { type: 'image/png' });
  var reader = new FileReader();
  reader.onloadend = function() {
    var dataURL = reader.result; // This will be a Data URL
    sketchDataUrl.value = dataURL;
  };
  reader.readAsDataURL(blob);
}
</script>

<template>
  <div class="__edit_exercise_form">
    <div class="wrapper_edited">
      <form class="form">
        <div class="column">
          <div class="input-box">
            <slot></slot>
            <label>{{ LocalConfig.FORM_TITLE_LABEL }}</label>
            <input
                :disabled="true"
                v-model="title"
                id="firstname"
                type="text"
                placeholder="Hechten II"
                required
            />
          </div>
          <div class="input-box">
            <label>{{ LocalConfig.FORM_CATEGORY_LABEL }}</label>
            <select :disabled="true" v-model="category" name="cars" id="cars">
              <option value="others">Sonstige</option>
              <optgroup label="Aufwärmen">
                <option value="coordination">{{ LocalConfig.CATEGORY_WARM_UP_COORDINATION }}</option>
                <option value="balance_ability">{{ LocalConfig.CATEGORY_WARM_UP_BALANCE_ABILITY }}</option>
                <option value="balance_ability">{{ LocalConfig.CATEGORY_WARM_UP_JUMP_COORDINATION }}</option>
              </optgroup>
              <optgroup label="Hauptteil">
                <option value="1vs1">{{ LocalConfig.CATEGORY_MAIN_1vs1 }}</option>
                <option value="pike">{{ LocalConfig.CATEGORY_MAIN_PIKE }}</option>
              </optgroup>
              <optgroup label="Abschluss">
                <option value="mercedes">Mercedes</option>
                <option value="audi">Audi</option>
              </optgroup>
            </select>
          </div>
          <div class="input-box">
            <label>{{ LocalConfig.FORM_NUMBER_OF_GOALKEEPER_LABEL }}</label>
            <input
                :disabled="true"
                v-model="numberOfGoalkeeper"
                type="number"
                placeholder="2"
                required
            />
          </div>
        </div>
        <div class="column">
          <div class="input-box">
            <label>{{ LocalConfig.FORM_DURATION_LABEL }}</label>
            <input
                :disabled="true"
                v-model="duration"
                onfocus="(this.type = 'time')"
                id="lastname"
                type="text"
                placeholder="00:15"
                required
            />
          </div>
          <div class="input-box">
            <label>{{ LocalConfig.FORM_INTENSITY_LABEL }}</label>
            <input
                :disabled="true"
                v-model="intensity"
                type="number"
                min="0"
                max="5"
                placeholder="0 (gering) - 5 (hoch)"
                required
            />
          </div>
          <div class="input-box" id="multi">
            <div id="material_container">
              <label style="width: 100%">{{ LocalConfig.FORM_MATERIAL_LABEL }}</label>
              <div v-for="(material, index) in materialList" :key="index" id="material_element" style="display: flex">
                <input :disabled="true" :value="material.numberOfMaterial" class="input" type="number" style="width: 25%; margin-right: 8px" placeholder="9" />
                <input :disabled="true" :value="material.material" class="input" type="text" />
              </div>
            </div>
          </div>
        </div>
        <br />
      </form>
      <div id="description_container">
        <form @submit="onSubmitClick" class="form" id="form">
          <label>{{ LocalConfig.FORM_DESCRIPTION_LABEL }}</label>
          <div v-for="description in descriptionList" id="description" style="display: flex">
            <textarea :value="description" class="input" id="textTest" rows="2"></textarea>
          </div>
        </form>
      </div>
      <br>
      <br>
      <div>
        <img :src="sketchDataUrl"/>
      </div>
      <br>
      <div v-if="isNoteNotEmpty">
        <br>
        <label>{{ LocalConfig.FORM_NOTES_LABEL }}</label>
        <textarea :value="note" class="input" id="textTest" rows="3"></textarea>
      </div>
    </div>
  </div>
</template>

<style scoped>
.__edit_exercise_form {
  display: grid;
}
.wrapper_edited {
  place-self: center;
  min-width: 100%;
  max-width: 100%;
  background: var(--lightGrey);
  text-align: left;
}
.form :where(.input-box input) {
  position: relative;
  height: 50px;
  width: 100%;
  margin-top: 8px;
}
img {
  width: 100%;
}
</style>