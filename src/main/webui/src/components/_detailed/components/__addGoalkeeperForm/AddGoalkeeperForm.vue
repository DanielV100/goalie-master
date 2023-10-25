<script setup>
import {computed, onMounted, ref} from "vue";
import * as LocalConfig from "./resources/addGoalkeeperFormConfig.js";
import CurrentPageIndicator from "@/components/_globals/components/__currentPageIndicator/CurrentPageIndicator.vue";
import SuccessAnimation from "@/components/_globals/components/__successAnimation/SuccessAnimation.vue";

const isNotSubmitted = ref(true);
const firstname = ref('');
const lastname = ref('');
const club = ref('');
//submit button is active when all mandatory fields are filled
const isSubmitDisabled = computed(() => {
  return !(firstname.value !== '' && lastname.value !== '' && club.value !== '');
});

function onSubmitClick() {
  isNotSubmitted.value = false;
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
            <input id="birthday" type="text" onfocus="(this.type = 'date')" placeholder="15.06.1969" />
          </div>
        </div>
        <div class="input-box">
          <label>{{ LocalConfig.FORM_NOTES }}</label>
          <input id="notes" type="text" placeholder="Wir brauchen Eier!" />
        </div>
        <button @click.prevent="onSubmitClick" :disabled="isSubmitDisabled">{{ LocalConfig.BUTTON_ADD_GOALKEEPER }}</button>
      </form>
    </div>
    <SuccessAnimation v-else>{{ LocalConfig.SUCCESS_MESSAGE }}</SuccessAnimation>
  </div>
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
</style>
