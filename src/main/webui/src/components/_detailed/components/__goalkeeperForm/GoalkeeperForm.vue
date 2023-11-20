<script setup>
/**
 * SFC for adding a goalkeeper
 * @author daniel
 */
/**** IMPORTS ****/
import {computed, ref, defineProps, onBeforeMount} from "vue";
import axios from "axios";
/**** CONFIGS ****/
import * as LocalConfig from "./resources/addGoalkeeperFormConfig.js";
/**** UTILITY FUNCTIONS ****/
import * as UtilityFunctions from "@/globals/utilityFunctions.js";
/**** COMPONENTS ****/
import CurrentPageIndicator from "@/components/_globals/components/__currentPageIndicator/CurrentPageIndicator.vue";
import SuccessAnimation from "@/components/_globals/components/__successAnimation/SuccessAnimation.vue";
import ErrorDialog from "@/components/_globals/components/__errorDialog/ErrorDialog.vue";

/**** VARIABLES ****/
const isNotSubmitted = ref(true);
const firstname = ref('');
const lastname = ref('');
const regexForValidatingInput = /^[A-Za-z]+$/;
const birthday = ref();
const club = ref('');
const notes = ref('');
const errorMessage = ref('');
const submitButtonText = ref(LocalConfig.BUTTON_ADD_GOALKEEPER);
const successMessage = ref(LocalConfig.SUCCESS_MESSAGE);

/**** COMPUTED PROPERTIES ****/
const isValidEntry = computed(() => {
  return regexForValidatingInput.test(firstname.value) && regexForValidatingInput.test(lastname.value);
});
//submit button is active when all mandatory fields are filled
const isSubmitDisabled = computed(() => {
  return !(firstname.value !== '' && lastname.value !== '' && club.value !== '' && isValidEntry.value);
});

/**** PROPS ****/
const props = defineProps({
  isEditView: Boolean,
  id: Number,
  firstname: String,
  lastname: String,
  birthday: String,
  club: String,
  notes: String
});

/**** HOOKS ****/
onBeforeMount(() => {
  if(props.isEditView) {
    firstname.value = props.firstname;
    lastname.value = props.lastname;
    birthday.value = props.birthday;
    club.value = props.club;
    notes.value = props.notes;
    submitButtonText.value = LocalConfig.BUTTON_ADD_GOALKEEPER_EDIT
    successMessage.value = LocalConfig.SUCCESS_MESSAGE_EDIT;
  }
});

/**** HTTTP-REQUESTS ****/
const isAddingGoalkeeperSuccessful = async () => {
  try {
    await axios.post('/goalkeeper/add', createGoalkeeperArrayForHttpRequest(), {
      headers: {
        Authorization: `Bearer ${UtilityFunctions.getJwtTokenFromSessionStorage()}`
      }
    });
    return true;
  } catch (error) {
    errorMessage.value = UtilityFunctions.errorHandling(error, LocalConfig.BUTTON_ADD_GOALKEEPER);
    resetForm();
    return false;
  }
}
const isUpdatingGoalkeeperSuccessful = async () => {
  try {
    await axios.put('/goalkeeper/update', createGoalkeeperArrayForHttpRequest(), {
      headers: {
        Authorization: `Bearer ${UtilityFunctions.getJwtTokenFromSessionStorage()}`
      }
    });
    return true;
  } catch (error) {
    errorMessage.value = UtilityFunctions.errorHandling(error, LocalConfig.BUTTON_ADD_GOALKEEPER);
    resetForm();
    return false;
  }
}

/**** CLICK-HANDLERS ****/
/**
 * Triggered when submit button is clicked.
 * @returns {Promise<void>}
 */
async function onSubmitClick() {
  UtilityFunctions.setLoadingCircleInSubmitButton();
  let isSuccessful;
  if(props.isEditView) {
    isSuccessful = await isUpdatingGoalkeeperSuccessful();
  } else {
    isSuccessful = await isAddingGoalkeeperSuccessful();
  }
  if(isSuccessful) {
    isNotSubmitted.value = false;
  }
}
/**** FUNCTIONS ****/
/**
 * Method for resetting form and refs.
 */
function resetForm() {
  firstname.value = '';
  lastname.value = '';
  birthday.value = '';
  club.value = '';
  notes.value = '';
}

/**
 * Creating object which is in the body of the http request
 * @returns object, which is used for the http request
 */
function createGoalkeeperArrayForHttpRequest() {
  const goalkeeper = {
    firstname: firstname.value,
    lastname: lastname.value,
    birthday: birthday.value,
    club: club.value,
    notes: notes.value
  };
  if(props.isEditView === true) {
    goalkeeper['id'] = props.id;
  }
  return goalkeeper;
}
</script>

<template>
  <div class="__add_goalkeeper_form">
    <CurrentPageIndicator>
      {{ LocalConfig.CURRENT_PAGE }}
    </CurrentPageIndicator>
    <div v-if="isNotSubmitted" class="wrapper">
      <form  class="form">
        <div class="column">
          <div class="input-box">
            <label>{{ LocalConfig.FORM_FIRSTNAME_LABEL }}*</label>
            <input
                v-model="firstname"
                id="firstname"
                type="text"
                placeholder="Oliver"
                required
            />
          </div>
          <div class="input-box">
            <label>{{ LocalConfig.FORM_LASTNAME_LABEL }}*</label>
            <input
                v-model="lastname"
                id="lastname"
                type="text"
                placeholder="Kahn"
                required
            />
          </div>
        </div>
        <div class="column">
          <div class="input-box">
            <label>{{ LocalConfig.FORM_CLUB }}*</label>
            <input
                v-model="club"
                id="club"
                type="text"
                placeholder="FC Bayern München"
                required
            />
          </div>
          <div class="input-box">
            <label>{{ LocalConfig.FORM_BIRTHDAY }}</label>
            <input v-model="birthday" id="birthday" type="text" onfocus="(this.type = 'date')" placeholder="15.06.1969" />
          </div>
        </div>
        <div class="input-box">
          <label>{{ LocalConfig.FORM_NOTES }}</label>
          <input v-model="notes" id="notes" type="text" placeholder="Wir brauchen Eier!" />
        </div>
        <button class="submit" @click.prevent="onSubmitClick" :disabled="isSubmitDisabled">{{ submitButtonText }}</button>
      </form>
    </div>
    <SuccessAnimation v-else>{{ successMessage }}</SuccessAnimation>
  </div>
  <ErrorDialog>{{ errorMessage }}</ErrorDialog>
</template>

<style scoped>
.__add_goalkeeper_form {
  display: grid;
}
.wrapper {
  text-align: left;
  max-width: 80%;
  min-width: 80%;
  background: var(--lightGrey);
}
#notes {
  padding-top: 20px;
  padding-bottom: 20px;
}
.error {
  background: lightcoral;
}
</style>
