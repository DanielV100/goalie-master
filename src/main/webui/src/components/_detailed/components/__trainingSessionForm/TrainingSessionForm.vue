
<script setup>
/**
 * SFC for creating and editing training session
 * @author daniel
 * @Important: Calling the accordions is only working correctly with passing a modalID.
 * Otherwise, the states of the three accordions appear interfering and some stupid things happen.
 * @TimeToFindOut: 120min.
 */

/**** IMPORTS ****/
import {computed, onBeforeMount, onMounted, ref} from "vue";
import axios from "axios";
/**** CONFIGS ****/
import * as LocalConfig from './resources/createTrainingSessionForm.js';
/**** UTILITY FUNCTIONS ****/
import * as UtilityFunctions from "@/globals/utilityFunctions";
import * as SessionStorageFunctions from "@/globals/sessionStorageUtilitiyFunctions.js";
import * as GetRequestFunctions from "@/globals/getRequestUtilityFunctions.js";
/**** COMPONENTS ****/
import CurrentPageIndicator from "@/components/_globals/components/__currentPageIndicator/CurrentPageIndicator.vue";
import Multiselect from "@/components/_globals/components/__multiselect/Multiselect.vue";
import Accordion from "@/components/_globals/components/__accordion/Accordion.vue";
import SuccessAnimation from "@/components/_globals/components/__successAnimation/SuccessAnimation.vue";
import ErrorDialog from "@/components/_globals/components/__errorDialog/ErrorDialog.vue";
/**** VARIABLES ****/
const title = ref('');
const date = ref('');
const note = ref('');
const goalkeepersNameAndId = ref([]);
const selectedGoalkeepers = ref([]);
const isNotSubmitted = ref(true);
const errorMessage = ref('');
const submitButtonText = ref(LocalConfig.BUTTON_CREATE_TRAINING_SESSION);
const successMessageText = ref(LocalConfig.SUCCESS_MESSAGE);

/**** COMPUTED PROPERTIES ****/
const isSubmitDisabled = computed(() => {
  console.log(date.value);
  return !(title.value !== '' && date.value !== '');
});

/**** PROPS ****/
const props = defineProps({
  isEditView: Boolean,
  id: Number,
  title: String,
  date: String,
  note: String,
  exercisesIdsWarmUp: Object,
  exercisesIdsMain: Object,
  exercisesIdsEnd: Object,
  goalkeepersIds: Object
});

/**** HOOKS ****/
onBeforeMount(() => {
  init();
});

onMounted(async () => {
  if(props.isEditView) {
    submitButtonText.value = LocalConfig.BUTTON_CREATE_TRAINING_SESSION_EDIT;
    successMessageText.value = LocalConfig.SUCCESS_MESSAGE_EDIT;
  }

  let goalkeepersFromDB = await GetRequestFunctions.getGoalkeepersFromDB();
  createGoalkeepersSelectable(goalkeepersFromDB);
  let exercises = await GetRequestFunctions.getAllExercisesFromDB();
});

/**** HTTP-REQUESTS ****/
const isCreatingTrainingSessionSuccessful = async () => {
  try {
    await axios.post('/training_session/create', createTrainingSessionObjectForResponse(), {
      headers: {
        Authorization: `Bearer ${SessionStorageFunctions.getJwtTokenFromSessionStorage()}`
      }
    });
    return true;
  } catch (error) {
    errorMessage.value = UtilityFunctions.errorHandling(error, LocalConfig.BUTTON_ADD_GOALKEEPER);
    return false;
  }
}

const isUpdatingExistingTrainingSessionSuccessful = async () => {
  try {
    await axios.put('/training_session/update', createTrainingSessionObjectForResponse(), {
      headers: {
        Authorization: `Bearer ${SessionStorageFunctions.getJwtTokenFromSessionStorage()}`
      }
    });
    return true;
  } catch (error) {
    errorMessage.value = UtilityFunctions.errorHandling(error, LocalConfig.BUTTON_ADD_GOALKEEPER);
    return false;
  }
}

/**** CLICK-HANDLERS ****/
/**
 * Triggered when submit button clicked
 * @returns {Promise<void>}
 */
async function onSubmitClick() {
  UtilityFunctions.setLoadingCircleInSubmitButton();
  let isSuccessful;
  if(props.isEditView) {
    isSuccessful = await isUpdatingExistingTrainingSessionSuccessful();
  } else {
    isSuccessful = await isCreatingTrainingSessionSuccessful();
  }
  if(isSuccessful) {
    isNotSubmitted.value = false;
  }
}

/**** FUNCTIONS ****/
function init() {
  if(props.isEditView) {
    title.value = props.title;
    date.value = props.date;
    note.value = props.note;
  }
}

/**
 * Create training session as object for http-response
 * @returns object
 */
function createTrainingSessionObjectForResponse() {
  const trainingSession = {
    title: title.value,
    date: date.value,
    notes: note.value,
    goalkeeperIds: getGoalkeeperIds(getSelectedGoalkeepers()),
    exerciseIds: getIdsFromCheckedExercises()
  };
  if(props.isEditView) {
    trainingSession['id'] = props.id;
  }
  return trainingSession;
}


/**
 * The multiselect component needs an object of arrays as prop, which gets build in this method.
 * @param goalkeepers
 */
function createGoalkeepersSelectable(goalkeepers) {
  goalkeepers.forEach((goalkeeper) => {
    if(props.isEditView) {
      for (let id of props.goalkeepersIds) {
        if(goalkeeper.id == id) {
          selectedGoalkeepers.value.push({name:goalkeeper.firstname + ' ' + goalkeeper.lastname, id:goalkeeper.id});
        }
      }
    }
    goalkeepersNameAndId.value.push({name:goalkeeper.firstname + ' ' + goalkeeper.lastname, id:goalkeeper.id});
  });
}

/**
 * Getting ids from selected goalkeepers via name comparison.
 * This has to be changed, when duplicate names are allowed.
 * @param goalkeepers
 * @returns {*[]}
 */
function getGoalkeeperIds(goalkeepers) {
  let goalkeeperIds = [];
  goalkeepers.forEach((goalkeeper) => {
    goalkeepersNameAndId.value.forEach((goalkeepersNameAndIdElement) => {
      if(goalkeepersNameAndIdElement.name === goalkeeper) {
        goalkeeperIds.push(goalkeepersNameAndIdElement.id);
      }
    });
  });
  console.log(goalkeeperIds);
  return goalkeeperIds;
}


/**
 * Unfortunately '@selected' is not working for multiselect.
 * @see https://vue-multiselect.js.org/#sub-events
 * So, selected goalkeepers are get via dom manipulation.
 * @returns {*[]}
 */
function getSelectedGoalkeepers() {
  let selectedGoalkeepers = [];
  const multiselectSelected = document.querySelector('.multiselect__tags-wrap').children;
  if(multiselectSelected === null || multiselectSelected === undefined) {
    return [];
  } else {
    for (const multiselectSelectedElement of multiselectSelected) {
      selectedGoalkeepers.push(multiselectSelectedElement.innerText);
    }
  }
  return  selectedGoalkeepers;
}


/**
 * Getting ids from checked exercises
 * @returns list of ids
 */
function getIdsFromCheckedExercises() {
  let exercisesIds = [];
  const checkedCheckboxes = document.querySelectorAll('input:checked');
  if(checkedCheckboxes === null || checkedCheckboxes === undefined) {
    return [];
  } else {
    checkedCheckboxes.forEach((checkbox) => {
      exercisesIds.push(checkbox.parentElement.nextSibling.innerText);
    });
  }
  return exercisesIds;
}
</script>

<template>
  <div class="__create_training_session_form">
    <CurrentPageIndicator>{{ LocalConfig.CURRENT_PAGE }}</CurrentPageIndicator>
    <div v-if="isNotSubmitted" class="wrapper">
      <form @submit="onSubmitClick" class="form">
        <div class="column">
          <div class="input-box">
            <label>{{ LocalConfig.FORM_TITLE_LABEL }}</label>
            <input
                v-model="title"
                type="text"
                placeholder="SVS - 1-g-1"
                required
            />
          </div>
          <div class="input-box">
            <label>{{ LocalConfig.FORM_DATE_LABEL }}</label>
            <input v-model="date" id="date" type="text" onfocus="(this.type = 'date')" placeholder="01.01.2022" required/>
          </div>
        </div>
        <br>
        <div>
          <label>{{ LocalConfig.FORM_CHOOSE_GOALKEEPER_LABEL }}</label>
          <Multiselect :preselected-values="selectedGoalkeepers" :multiselect-options="goalkeepersNameAndId"/>
        </div>
      </form>
      <br>
      <Accordion :is-edit-view="props.isEditView" :exercises-ids="props.exercisesIdsWarmUp" category="warmUp" modal-i-d="_warmup">Aufwärmen</Accordion>
      <br>
      <br>
      <Accordion :is-edit-view="props.isEditView" :exercises-ids="props.exercisesIdsMain" category="main" modal-i-d="_main">Hauptteil</Accordion>
      <br>
      <br>
      <Accordion :is-edit-view="props.isEditView" :exercises-ids="props.exercisesIdsEnd" category="end" modal-i-d="_end">Abschluss</Accordion>
      <br>
      <div>
        <br>
        <label>Notizen</label>
        <textarea class="input" v-model="note" id="textTest" rows="3"></textarea>
      </div>
      <button class="submit" @click.prevent="onSubmitClick" :disabled="isSubmitDisabled">{{ submitButtonText }}</button>
      </div>
      <SuccessAnimation v-else>{{ successMessageText }}</SuccessAnimation>
    </div>
  <ErrorDialog>{{ errorMessage }}</ErrorDialog>
</template>

<style scoped>
.__create_training_session_form {
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

#multi button {
  margin-top: 10pt;
}

button {
  width: 100%;
}
</style>