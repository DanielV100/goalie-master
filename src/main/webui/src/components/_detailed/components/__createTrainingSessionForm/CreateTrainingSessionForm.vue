
<script setup>
/**
 * @Important: Calling the accordions is only working correctly with passing a modalID.
 * Otherwise, the states of the three accordions appear interfering and some stupid things happen.
 * @TimeToFindOut: 120min.
 */
import CurrentPageIndicator from "@/components/_globals/components/__currentPageIndicator/CurrentPageIndicator.vue";
import * as LocalConfig from './resources/createTrainingSessionForm.js';
import Multiselect from "@/components/_globals/components/__multiselect/Multiselect.vue";
import Accordion from "@/components/_globals/components/__accordion/Accordion.vue";
import {computed, onBeforeMount, onMounted, ref} from "vue";
import axios from "axios";
import * as UtilityFunctions from "@/globals/utilityFunctions";
import * as SessionStorageFunctions from "@/globals/sessionStorageUtilitiyFunctions.js";
import * as GetRequestFunctions from "@/globals/getRequestUtilityFunctions.js";
const title = ref('');
const date = ref('');
const note = ref('');
const goalkeepersNameAndId = ref([]);

const props = defineProps({
  isEditView: Boolean,
  id: Number,
  title: String,
  date: String,
  note: String,
  exercisesIds: Object,
  goalkeepersIds: Object,
});

onBeforeMount(() => {
  init();
})
function init() {
  if(props.isEditView) {
    title.value = props.title;
    date.value = props.date;
    note.value = props.note;
  }
}

onMounted(async () => {
  let goalkeepersFromDB = await GetRequestFunctions.getGoalkeepersFromDB();
  createGoalkeepersSelectable(goalkeepersFromDB);

  let exercises = await GetRequestFunctions.getAllExercisesFromDB();
});

const isSubmitDisabled = computed(() => {
  console.log(date.value);
  return !(title.value !== '' && date.value !== '');
});

const isCreatingTrainingSessionSuccessful = async () => {
  try {
    const trainingSession = {
      title: title.value,
      date: date.value,
      notes: note.value,
      goalkeeperIds: getGoalkeeperIds(getSelectedGoalkeepers()),
      exerciseIds: getIdsFromCheckedExercises()
    };
    await axios.post('/training_session/create', trainingSession, {
      headers: {
        Authorization: `Bearer ${UtilityFunctions.getJwtTokenFromSessionStorage()}`
      }
    });
    return true;
  } catch (error) {
   //errorMessage.value = UtilityFunctions.errorHandling(error, LocalConfig.BUTTON_ADD_GOALKEEPER);
    //resetForm();
    return false;
  }
}
async function onSubmitClick() {
  UtilityFunctions.setLoadingCircleInSubmitButton();
  const isSuccessful = await isCreatingTrainingSessionSuccessful();
  if(isSuccessful) {
    console.log(isSuccessful);
    //isNotSubmitted.value = false;
  }
}

/**
 * The multiselect component needs an object of arrays as prop, which gets build in this method.
 * @param goalkeepers
 */
function createGoalkeepersSelectable(goalkeepers) {
  goalkeepers.forEach((goalkeeper) => {
    goalkeepersNameAndId.value.push({name:goalkeeper.firstname + ' ' + goalkeeper.lastname, id:goalkeeper.id});
  })
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
    <div class="wrapper">
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
          <Multiselect :multiselect-options="goalkeepersNameAndId"/>
        </div>
      </form>
      <br>
      <Accordion category="warmUp" modal-i-d="_warmup">Aufwärmen</Accordion>
      <br>
      <br>
      <Accordion category="main" modal-i-d="_main">Hauptteil</Accordion>
      <br>
      <br>
      <Accordion category="end" modal-i-d="_end">Abschluss</Accordion>
      <br>
      <div>
        <br>
        <label>Notizen</label>
        <textarea class="input" v-model="note" id="textTest" rows="3"></textarea>
      </div>
      <button class="submit" @click.prevent="onSubmitClick" :disabled="isSubmitDisabled">{{ LocalConfig.BUTTON_CREATE_TRAINING_SESSION }}</button>
    </div>
  </div>
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