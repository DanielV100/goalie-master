<script setup>
import * as LocalConfig from './resources/addExerciseFormConfig.js';
import * as UtilityFunctions from '../../../../globals/utilityFunctions.js';
import CurrentPageIndicator from "@/components/_globals/components/__currentPageIndicator/CurrentPageIndicator.vue";
import SoccerField from "@/components/_globals/components/__soccerField/SoccerField.vue";
import {computed, onBeforeMount, ref} from "vue";
import SuccessAnimation from "@/components/_globals/components/__successAnimation/SuccessAnimation.vue";
import axios from "axios";
import ErrorDialog from "@/components/_globals/components/__errorDialog/ErrorDialog.vue";

const isNotSubmitted = ref(true);
const isSketchCheckboxChecked = ref();
const isNotesCheckboxChecked = ref();
const title = ref('');
const category = ref('');
const numberOfGoalkeeper = ref('');
const duration = ref('');
const intensity = ref('');
const note = ref('');
const errorMessage = ref('');
const materialList = ref([]);
const descriptionList = ref([]);
const sketchDataUrl = ref();

const props = defineProps({
  isEditView: Boolean,
  id: Number,
  title: String,
  category: String,
  numberOfGoalkeeper: String,
  duration: String,
  intensity: String,
  note: String,
  materials: Object,
  numbersOfMaterial: Object,
  descriptionSteps: Object,
  sketch: Object
});

onBeforeMount(() => {
  init();
});
function init() {
  if(props.isEditView) {
    title.value = props.title;
    category.value = props.category;
    numberOfGoalkeeper.value = props.numberOfGoalkeeper;
    duration.value = props.duration;
    intensity.value = props.intensity;
    note.value = props.note;
    let test = [];
    for(let i = 0; i < props.numbersOfMaterial.length; i++) {
      test.push({numberOfMaterial: props.numbersOfMaterial[i], material: props.materials[i]});
    }
    materialList.value = test;
    descriptionList.value = props.descriptionSteps;
    byteArrayToDataURL(props.sketch)
  }
}
function byteArrayToDataURL(byteArray) {
  let blob = new Blob([new Uint8Array(byteArray)], { type: 'image/png' });
  var reader = new FileReader();
  reader.onloadend = function() {
    var dataURL = reader.result;
    sketchDataUrl.value = dataURL;
  };
  reader.readAsDataURL(blob);
}
const isSubmitDisabled = computed(() => {
  return !(title.value !== '' && category.value !== '' && numberOfGoalkeeper.value !== '' && duration.value !== '' && intensity.value !== '');
});
const isAddingExerciseSuccessful = async () => {
  try {
    await axios.post('/exercise/add', createExerciseForHttpRequest(), {
      headers: {
        Authorization: `Bearer ${UtilityFunctions.getJwtTokenFromSessionStorage()}`
      }
    });
    return true;
  } catch (error) {
    errorMessage.value = UtilityFunctions.errorHandling(error, LocalConfig.FORM_CREATE_EXERCISE_BUTTON);
    resetForm();
    return false;
  }
}

const isUpdatingExerciseSuccessful = async () => {
  try {
    await axios.put('/exercise/update', createExerciseForHttpRequest(), {
      headers: {
        Authorization: `Bearer ${UtilityFunctions.getJwtTokenFromSessionStorage()}`
      }
    });
    return true;
  } catch (error) {
    errorMessage.value = UtilityFunctions.errorHandling(error, LocalConfig.FORM_CREATE_EXERCISE_BUTTON);
    resetForm();
    return false;
  }
}
function getCategoryGroup() {
  const selectedOption = document.querySelector('select[name="categories"] option:checked');
  if(selectedOption === null) {
    return '';
  } else {
    return selectedOption.parentElement.label;
  }
}
/**
 * Method sets all v-models from form empty.
 */
function resetForm() {
  isSketchCheckboxChecked.value = false;
  isNotesCheckboxChecked.value = false;
  title.value = '';
  category.value = '';
  numberOfGoalkeeper.value = '';
  duration.value = '';
  intensity.value = '';
  note.value = '';
}
function addMaterialButtonClicked(event) {
  UtilityFunctions.cloneExistingFieldsInContainer(event, 'material_container', 'material_element');
}

function addDescriptionStepClicked(event) {
  UtilityFunctions.cloneExistingFieldsInContainer(event, 'description_container', 'description');
}

/**
 * Click handler for submit button.
 * @returns {Promise<void>}
 */
async function submitButtonClicked() {
  UtilityFunctions.setLoadingCircleInSubmitButton();
  let isSuccessful;
  if(props.isEditView) {
    isSuccessful = await isUpdatingExerciseSuccessful();
  } else {
    isSuccessful = await isAddingExerciseSuccessful();
  }
  if(isSuccessful) {
    isNotSubmitted.value = false;
  } else {

  }
}

function createExerciseForHttpRequest() {
  const exercise = {
    sketchDataURL:getDataUrlFromSketch(),
    title: title.value,
    category: category.value,
    categoryGroup: getCategoryGroup(),
    numberOfGoalkeepers: numberOfGoalkeeper.value,
    duration: duration.value,
    intensity: intensity.value,
    materials:getValueListFromDomInputFields('.material'),
    numbersOfMaterial:getValueListFromDomInputFields('.number_of_material'),
    descriptionSteps: getValueListFromDomInputFields('.description_step'),
    note: note.value
  };
  if(props.isEditView === true) {
    exercise['id'] = props.id;
  }
  return exercise;
}

/**
 * This method generates a list of values from a list of DOM input fields.
 * Needed for e.g. the materials, because there can be more than just one material.
 * @param selector
 * @returns {*[]}
 */
function getValueListFromDomInputFields(selector) {
  const inputFields = document.querySelectorAll(selector);
  if(inputFields === null) {
    return null;
  } else {
    let inputs = [];
    inputFields.forEach((inputField) => {
      const inputFieldValue = inputField.value;
      //empty values would lead to NumberFormatException on serverside
      if(inputFieldValue === '') {
        return null;
      } else {
        inputs.push(inputField.value);
      }
    });
    return inputs;
  }
}

/**
 * Getting data url from sketch (which is an HTML-canvas).
 * @returns {string|null}
 */
function getDataUrlFromSketch() {
  const sketch = document.querySelector('canvas');
  if(sketch === null) {
    return null;
  } else {
    return sketch.toDataURL('image/png')
  }
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
            <select v-model="category" name="categories" id="cars">
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
              <div v-if="props.isEditView"  v-for="(material, index) in materialList" id="material_element" style="display: flex">
                <input :value="material.numberOfMaterial" class="input number_of_material" type="number" style="width: 25%; margin-right: 8px" placeholder="9" />
                <input
                    :value="material.material"
                    class="input material"
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
              <div v-else id="material_element" style="display: flex">
                <input  class="input number_of_material" type="number" style="width: 25%; margin-right: 8px" placeholder="9" />
                <input
                    class="input material"
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
          <div v-if="props.isEditView" v-for="description in descriptionList"  id="description" style="display: flex">
            <textarea :value="description" class="input description_step" id="textTest" rows="2"></textarea>
            <button class="deleteButton" style="width: 10%;  margin-left: 8px; padding: 0 15px; margin-top: 8px; height: auto; " >-</button>
          </div>
          <div v-else  id="description" style="display: flex">
            <textarea class="input description_step" id="textTest" rows="2"></textarea>
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
      <div>
        <img v-if="!isSketchCheckboxChecked" id="sketchImage" :src="sketchDataUrl"/>
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
        <textarea class="input" v-model="note" id="textTest" rows="3"></textarea>
      </div>
      <button class="submit"  @click.prevent="submitButtonClicked" :disabled="isSubmitDisabled">{{ LocalConfig.FORM_CREATE_EXERCISE_BUTTON }}</button>
    </div>
    <SuccessAnimation v-else>{{ LocalConfig.SUCCESS_MESSAGE }}</SuccessAnimation>
  </div>
  <ErrorDialog>{{ errorMessage }}</ErrorDialog>
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