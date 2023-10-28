<script setup>
import * as LocalConfig from './resources/addExerciseFormConfig.js';
import * as UtilityFunctions from '../../../../globals/utilityFunctions.js';
import CurrentPageIndicator from "@/components/_globals/components/__currentPageIndicator/CurrentPageIndicator.vue";
import SoccerField from "@/components/_globals/components/__soccerField/SoccerField.vue";
import {computed, ref} from "vue";
import SuccessAnimation from "@/components/_globals/components/__successAnimation/SuccessAnimation.vue";

const isNotSubmitted = ref(true);
const isSketchCheckboxChecked = ref();
const isNotesCheckboxChecked = ref();
const title = ref('');
const category = ref('');
const numberOfGoalkeeper = ref('');
const duration = ref('');
const intensity = ref('');
const materialCount = ref('');
const material = ref('');

const numberOfMaterials = ref(['']);

const isSubmitDisabled = computed(() => {
  console.log(material.value);
  return !(title.value !== '' && category.value !== '' && numberOfGoalkeeper.value !== '' && duration.value !== '' && intensity.value !== '' && materialCount.value !== '' && material.value !== '' );
});

function addMaterialButtonClicked(event) {
  UtilityFunctions.cloneExistingFieldsInContainer(event, 'material_container', 'material_element');
}

function addDescriptionStepClicked(event) {
  UtilityFunctions.cloneExistingFieldsInContainer(event, 'description_container', 'description');
}

function submitButtonClicked() {
  isNotSubmitted.value = false;
}
</script>

<template>
  <div class="__add_exercise_form">
    <CurrentPageIndicator>{{ LocalConfig.CURRENT_PAGE }}</CurrentPageIndicator>
    <div v-if="isNotSubmitted" class="wrapper">
      <form @submit="onSubmitClick" class="form">
        <div class="column">
          <div class="input-box">
            <label>{{ LocalConfig.FORM_TITLE_LABEL }}</label>
            <input
                v-model="title"
                id="firstname"
                type="text"
                placeholder="Hechten II"
                required
            />
          </div>
          <div class="input-box">
            <label>{{ LocalConfig.FORM_CATEGORY_LABEL }}</label>
            <select v-model="category" name="cars" id="cars">
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
              <div v-for="x in numberOfMaterials" id="material_element" style="display: flex">
                <input  v-model="material" class="input" type="number" style="width: 25%; margin-right: 8px" placeholder="9" />
                <input
                    v-model="materialCount"
                    class="input"
                    type="text"
                    style="width: 60%;"
                    placeholder="Stangen"
                    list="materials"
                    required
                />
                <button class="deleteButton" style="width: 15%;  margin-left: 8px; padding: 0 15px; margin-top: 8px; height: auto; " >-</button>
                <datalist id="materials">
                  <option>Stangen</option>
                  <option>Hütchen</option>
                  <option>Bälle</option>
                  <option>Pylonen</option>
                </datalist>
              </div>
            </div>
            <button type="button" @click="addMaterialButtonClicked">+</button>
          </div>
        </div>
        <br />
      </form>
      <div id="description_container">
        <form @submit="onSubmitClick" class="form" id="form">
          <label>{{ LocalConfig.FORM_DESCRIPTION_LABEL }}</label>
          <div id="description" style="display: flex">
            <textarea class="input" id="textTest" rows="2"></textarea>
            <button class="deleteButton" style="width: 10%;  margin-left: 8px; padding: 0 15px; margin-top: 8px; height: auto; " >-</button>
          </div>
        </form>
      </div>
      <button @click="addDescriptionStepClicked">+</button>
      <br>
      <br>
      <div>
        <div>
          <label class="form-control">
            <input type="checkbox" name="checkbox-checked" v-model="isSketchCheckboxChecked" />
            {{ LocalConfig.FORM_SKETCH_LABEL }}
          </label>
        </div>
      </div>
      <div v-if="isSketchCheckboxChecked" id="soccerFieldContainer">
        <SoccerField />
      </div>
      <br>
      <div>
        <div>
          <label class="form-control">
            <input type="checkbox" name="checkbox-checked" v-model="isNotesCheckboxChecked" />
            {{ LocalConfig.FORM_ADD_NOTES_LABEL }}
          </label>
        </div>
      </div>
      <div v-if="isNotesCheckboxChecked">
        <br>
        <label>{{ LocalConfig.FORM_NOTES_LABEL }}</label>
        <textarea class="input" id="textTest" rows="3"></textarea>
      </div>
      <button @click.prevent="submitButtonClicked" :disabled="isSubmitDisabled">{{ LocalConfig.FORM_CREATE_EXERCISE_BUTTON }}</button>
    </div>
    <SuccessAnimation v-else>{{ LocalConfig.SUCCESS_MESSAGE }}</SuccessAnimation>
  </div>
</template>

<style scoped>
.__add_exercise_form {
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