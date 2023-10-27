<script setup>
import CurrentPageIndicator from "@/components/_globals/components/__currentPageIndicator/CurrentPageIndicator.vue";
import * as LocalConfig from './resources/createTrainingSessionForm.js';
import {onMounted} from "vue";
import Multiselect from "@/components/_globals/components/__multiselect/Multiselect.vue";
import Accordion from "@/components/_globals/components/__accordion/Accordion.vue";
import EditExerciseForm from "@/components/_detailed/components/__addExerciseForm/ViewExerciseForm.vue";

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
            <input
                v-model="title"
                id="firstname"
                type="text"
                placeholder="Hechten II"
                required
            />
          </div>
        </div>
        <br>
        <div>
          <label>{{ LocalConfig.FORM_CHOOSE_GOALKEEPER_LABEL }}</label>
          <Multiselect/>
        </div>
      </form>
      <br>
      <Accordion>Aufwärmen</Accordion>
      <br>
      <Accordion>Hauptteil</Accordion>
      <br>
      <Accordion>Abschluss</Accordion>
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