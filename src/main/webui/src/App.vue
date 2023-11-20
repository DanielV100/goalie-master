<script setup>
/**
 * "Main"-Class for getting the Vue app ready. Routing is done here.
 * @author daniel
 */

/**** IMPORTS ****/
import {computed, ref} from "vue";
/**** COMPONENTS ****/
import LoginForm from "@/components/_login/components/__login_form/LoginForm.vue";
import Header from "@/components/_globals/components/__header/Header.vue";
import Overview from "@/components/_menu/components/__overview/Overview.vue";
import AddGoalkeeperForm from "@/components/_detailed/components/__goalkeeperForm/GoalkeeperForm.vue";
import AddExerciseForm from "@/components/_detailed/components/__exerciseForm/ExerciseForm.vue";
import CreateTrainingSessionForm from "@/components/_detailed/components/__trainingSessionForm/TrainingSessionForm.vue";
import GeneralOverview from "@/components/_detailed/components/__generalOverview/GeneralOverview.vue";
import NotFound from "@/components/_globals/components/__notFound/NotFound.vue";
/**** UTILITY FUNCTIONS ****/
import * as JwtTokenUtility from '@/globals/jwtTokenUtility.js';

/**** VARIABLES ****/
const isShowingHeader = ref(false);
const currentPath = ref(window.location.hash);
const routes = {
  '/': LoginForm,
  '/menu': Overview,
  '/add-goalkeeper': AddGoalkeeperForm,
  '/add-exercise': AddExerciseForm,
  '/create-training-session': CreateTrainingSessionForm,
  '/general-overview': GeneralOverview
};

/**** COMPUTED PROPERTIES ****/
const currentView = computed(() => {
  console.log(JwtTokenUtility.checkIfJwtTokenIsValid());
  const current = routes[currentPath.value.slice(1) || '/'];
  if(current) {
    //check if user is authenticated
    if(current.__name !== 'LoginForm' && !JwtTokenUtility.checkIfJwtTokenIsValid()) {
      return NotFound;
    }
    //header shouldn't be shown in login page
    if(current.__name !== 'LoginForm') {
      isShowingHeader.value = true;
    } else {
      isShowingHeader.value = false;
    }
  }
  return  current || NotFound;
});

/**** EVENT LISTENERS ****/
window.addEventListener('hashchange', () => {
  currentPath.value = window.location.hash;
});
</script>

<template>
  <Header v-if="isShowingHeader"/>
  <component :is="currentView" />
</template>
