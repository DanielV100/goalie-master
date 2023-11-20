<script setup>
/**
 * SFC for adding and edit an exercise
 * @author daniel
 */
/**** IMPORTS ****/
import {computed, onBeforeMount, onMounted, ref} from "vue";
import axios from "axios";
/**** CONFIGS ****/
import * as LocalConfig from './resources/addExerciseFormConfig.js';
/**** UTILITY FUNCTIONS ****/
import * as UtilityFunctions from '@/globals/utilityFunctions.js';
import * as SessionStorageFunctions from '@/globals/sessionStorageUtilitiyFunctions.js';
/**** COMPONENTS ****/
import CurrentPageIndicator from "@/components/_globals/components/__currentPageIndicator/CurrentPageIndicator.vue";
import SoccerField from "@/components/_globals/components/__soccerField/SoccerField.vue";
import SuccessAnimation from "@/components/_globals/components/__successAnimation/SuccessAnimation.vue";
import ErrorDialog from "@/components/_globals/components/__errorDialog/ErrorDialog.vue";

/**** VARIABLES ****/
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
const isMaterialDeleteButtonDisabled = ref(true);
const isDescriptionStepDeleteButtonDisabled = ref(true);
const createExerciseButtonText = ref(LocalConfig.FORM_CREATE_EXERCISE_BUTTON);
const successMessageText = ref(LocalConfig.SUCCESS_MESSAGE)

/**** COMPUTED PROPERTIES ****/
const isSubmitDisabled = computed(() => {
  return !(title.value !== '' && category.value !== '' && numberOfGoalkeeper.value !== '' && duration.value !== '' && intensity.value !== '');
});

/**** PROPS ****/
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
  sketch: String
});

/**** HOOKS ****/
onBeforeMount(() => {
  init();
});

onMounted(() => {

  //making material and descriptions steps deletable
  if(props.isEditView) {
    successMessageText.value = LocalConfig.SUCCESS_MESSAGE_EDIT;
    createExerciseButtonText.value = LocalConfig.FORM_CREATE_EXERCISE_BUTTON_EDIT;
    UtilityFunctions.bindDeleteEventListenerToAllDeleteButtons();
  }
});

/**** HTTP-REQUESTS ****/
const isAddingExerciseSuccessful = async () => {
  try {
    await axios.post('/exercise/add', createExerciseForHttpRequest(), {
      headers: {
        Authorization: `Bearer ${SessionStorageFunctions.getJwtTokenFromSessionStorage()}`
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
        Authorization: `Bearer ${SessionStorageFunctions.getJwtTokenFromSessionStorage()}`
      }
    });
    return true;
  } catch (error) {
    errorMessage.value = UtilityFunctions.errorHandling(error, LocalConfig.FORM_CREATE_EXERCISE_BUTTON);
    resetForm();
    return false;
  }
}

/**** CLICK-HANDLERS ****/

/**
 * Triggered by clicking submit button
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
  }
}

/**
 * Triggered when adding material add button is clicked
 * @param event which triggered the click handler
 */
function addMaterialButtonClicked(event) {
  UtilityFunctions.cloneExistingFieldsInContainer(event, 'material_container', 'material_element');
}

/**
 * Triggered when description steps add button is clicked
 * @param event which triggered the click handler
 */
function addDescriptionStepClicked(event) {
  UtilityFunctions.cloneExistingFieldsInContainer(event, 'description_container', 'description');
}

/**** FUNCTIONS ****/

/**
 * Initialises form and fills form if sfc is called in edit view
 */
function init() {
  if(props.isEditView) {

    title.value = props.title;
    category.value = props.category;
    numberOfGoalkeeper.value = props.numberOfGoalkeeper;
    duration.value = props.duration;
    intensity.value = props.intensity;
    note.value = props.note;
    //material list requires list with amount and description
    let materialListTemp = [];
    for(let i = 0; i < props.numbersOfMaterial.length; i++) {
      materialListTemp.push({numberOfMaterial: props.numbersOfMaterial[i], material: props.materials[i]});
    }
    materialList.value = materialListTemp;
    if(Object.keys(props.materials).length > 1) {
      isMaterialDeleteButtonDisabled.value = false;
    }
    descriptionList.value = props.descriptionSteps;
    if(Object.keys(props.descriptionSteps).length > 1) {
      isDescriptionStepDeleteButtonDisabled.value = false;
    }
    sketchDataUrl.value = props.sketch;
  }
}

/**
 * Getting category group from the exercise out dom
 * @returns {*|string}
 */
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

/**
 * Method for creating object, which will get send in the body of http request
 * @returns object
 */
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
      <form class="form">
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
            <select v-model="category" name="categories" required>
              <optgroup label="Aufwärmen">
                <option value="coordination">{{ LocalConfig.CATEGORY_WARM_UP_COORDINATION }}</option>
                <option value="balance_ability">{{ LocalConfig.CATEGORY_WARM_UP_BALANCE_ABILITY }}</option>
                <option value="balance_ability">{{ LocalConfig.CATEGORY_WARM_UP_JUMP_COORDINATION }}</option>
                <option value="agility">{{ LocalConfig.CATEGORY_WARM_UP_AGILITY }}</option>
                <option value="flexibility">{{ LocalConfig.CATEGORY_WARM_UP_FLEXIBILITY }}</option>
                <option value="other">{{ LocalConfig.OTHER }}</option>
              </optgroup>
              <optgroup label="Hauptteil">
                <option value="1vs1">{{ LocalConfig.CATEGORY_MAIN_1vs1 }}</option>
                <option value="pike">{{ LocalConfig.CATEGORY_MAIN_PIKE }}</option>
                <option value="positioning">{{ LocalConfig.CATEGORY_MAIN_POSITIONING }}</option>
                <option value="reflexes">{{ LocalConfig.CATEGORY_MAIN_REFLEXES }}</option>
                <option value="other">{{ LocalConfig.OTHER }}</option>
              </optgroup>
              <optgroup label="Abschluss">
                <option value="cool_down_stretch">{{ LocalConfig.CATEGORY_COOL_DOWN_STRETCH }}</option>
                <option value="mental_relaxation">{{ LocalConfig.CATEGORY_COOL_DOWN_MENTAL_RELAXATION }}</option>
                <option value="other">{{ LocalConfig.OTHER }}</option>
              </optgroup>
            </select>
          </div>
          <div class="input-box">
            <label>{{ LocalConfig.FORM_NUMBER_OF_GOALKEEPER_LABEL }}</label>
            <input
                v-model="numberOfGoalkeeper"
                step="1"
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
              <div v-if="props.isEditView"  v-for="(material) in materialList" id="material_element" style="display: flex">
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
                <button class="deleteButton" id="delete_material" :disabled="isMaterialDeleteButtonDisabled" style="width: 15%;  margin-left: 8px; padding: 0 15px; margin-top: 8px; height: auto; " >-</button>
                <datalist id="materials">
                  <option>Stangen</option>
                  <option>Hütchen</option>
                  <option>Bälle</option>
                  <option>Pylonen</option>
                  <option>Hürden</option>
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
                <button class="deleteButton" :disabled="isMaterialDeleteButtonDisabled" id="delete_material" style="width: 15%;  margin-left: 8px; padding: 0 15px; margin-top: 8px; height: auto; " >-</button>
                <datalist id="materials">
                  <option>Stangen</option>
                  <option>Hütchen</option>
                  <option>Bälle</option>
                  <option>Pylonen</option>
                </datalist>
              </div>
            </div>
            <button id="addMaterial" type="button" @click="addMaterialButtonClicked">+</button>
          </div>
        </div>
        <br />
      </form>
      <div id="description_container">
        <form class="form" id="form">
          <label>{{ LocalConfig.FORM_DESCRIPTION_LABEL }}</label>
          <div v-if="props.isEditView" v-for="description in descriptionList"  id="description" style="display: flex">
            <textarea :value="description" class="input description_step" id="textTest" rows="2"></textarea>
            <button @p.prevent id="delete_description" :disabled="isDescriptionStepDeleteButtonDisabled" class="deleteButton" style="width: 10%;  margin-left: 8px; padding: 0 15px; margin-top: 8px; height: auto; " >-</button>
          </div>
          <div v-else  id="description" style="display: flex">
            <textarea class="input description_step" id="textTest" rows="2"></textarea>
            <button id="delete_description" :disabled="isDescriptionStepDeleteButtonDisabled" class="deleteButton" style="width: 10%;  margin-left: 8px; padding: 0 15px; margin-top: 8px; height: auto; " >-</button>
          </div>
        </form>
      </div>
      <button id="addDescription" @click="addDescriptionStepClicked">+</button>
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
      <button class="submit"  @click.prevent="submitButtonClicked" :disabled="isSubmitDisabled">{{ createExerciseButtonText }}</button>
    </div>
    <SuccessAnimation v-else>{{ successMessageText }}</SuccessAnimation>
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

img {
  width: 100%;
}
</style>