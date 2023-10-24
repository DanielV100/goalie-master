<script setup>
import { onMounted } from 'vue';
import * as LocalConfig from './resources/overviewConfig.js';


const username = "daniel.vollmer!";
let i = 0;

onMounted(() => {
  typeWriter();
});


function typeWriter() {
  if (i < username.length) {
    document.querySelector(".welcomeName").innerHTML += username.charAt(i);
    i++;
    setTimeout(typeWriter, LocalConfig.TYPEWRITTERSPEED);
  }
}

function addGoalkeeperClicked() {

}
function callPageForAddingNewGoalie() {
  window.location = '#/addgoalie';
}


</script>

<template>
  <div class="__overview">
      <div class="welcome_area">
        <p class="welcomeMessage"> {{ LocalConfig.GREETING }}</p>
        <p class="welcomeName"></p>
        <h2>{{ LocalConfig.WELCOME_MESSAGE }} </h2>
      </div>
      <div class="wrapper">
        <button class="custom-button">{{ LocalConfig.TO_DASHBOARD }}</button>
        <button class="custom-button">{{ LocalConfig.ADD_EXERCISE }}</button>
        <button class="custom-button">{{ LocalConfig.ADD_SESSION }}</button>
        <button @click="addGoalkeeperClicked" class="custom-button">{{ LocalConfig.ADD_GOALKEEPER }}</button>
      </div>
  </div>
</template>

<style scoped>
.__overview {
  display: grid;
}
.welcome_area {
  place-self: center;
  padding: 12pt;
  display: inline-block;
  margin-bottom: auto;
}
.welcomeMessage {
  display: inline;
  font-size: var(--bigHeadingFontSize);
  font-weight: 200;
}
.welcomeName {
  display: inline;
  font-size: var(--bigHeadingFontSize);
  font-weight: 800;
  color: var(--avocadoGreen)
}
.wrapper {
  min-width: 80%;
  max-width: 80%;
  background: var(--lightGrey);
  padding: 20px 20px 10px 20px;
}
.custom-button {
  width: calc(50% - 20px);
  margin: 0px 20px 20px 0px;
  border: none;
  border-radius: 5px;
  padding: 30px;
}
.custom-button:hover {
  text-align: left;
  font-weight: bold;
}

</style>