<script setup>
import {computed, ref} from 'vue';
import * as GlobalConfig from '../../../../globals/gloablConfig.js';
import * as LocalConfig from './resources/loginFormConfig.js';
import LoaderAnimation from "@/components/_login/components/__loader_animation/LoaderAnimation.vue";

const username = ref('');
const password = ref('');
const isPasswordInvisible = ref(true);
const isLoginFormVisible = ref(true);

//Button is only clickable, when username and password is set
const isLoginButtonDisabled = computed(() => {
  console.log(username.value);
  return !(username.value !== '' && password.value !== '');
});

function loginButtonClicked() {
  showLoaderAnimation();
}

/**
 * Setting boolean false, to display login loader.
 */
function showLoaderAnimation() {
  isLoginFormVisible.value = false;
}
function passwordIconClicked() {
  togglePasswordIcon();
}

/**
 * Function used for setting the lock opend/closed in password field.
 */
function togglePasswordIcon() {
  if(isPasswordInvisible.value === true) {
    setPasswordVisibility("text");
    isPasswordInvisible.value = false;
  } else {
    setPasswordVisibility("password");
    isPasswordInvisible.value = true;
  }
}

/**
 * Getting the password input from DOM and set it visible/invisible through type attribute.
 * @param {'password', 'text'} string
 */
function setPasswordVisibility(type) {
  document.getElementById('password').setAttribute("type", type);
}
</script>

<template>
  <div class="__loginForm">
    <div v-if="isLoginFormVisible" class="wrapper">
      <form action="">
        <h1>{{ GlobalConfig.APP_NAME }}</h1>
        <p class="dedication">{{ LocalConfig.DEDICATION_TEXT }}</p>
        <div class="input-box">
          <input v-model="username" type="text" placeholder="Username" required>
          <i class='bx bxs-user'></i>
        </div>
        <div class="input-box">
          <input v-model="password" id="password" type="password" placeholder="Password" required>
          <i v-if="isPasswordInvisible" @click="passwordIconClicked"  class='bx bxs-lock-alt'></i>
          <i v-else @click="passwordIconClicked" class='bx bxs-lock-open-alt' ></i>
        </div>
        <button type="submit" :disabled="isLoginButtonDisabled" @click="loginButtonClicked">Login</button>
      </form>
    </div>
    <div v-else class="wrapper">
      <LoaderAnimation />
    </div>
  </div>
</template>

<style>
.__loginForm {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: url('resources/images/loginFormBackground.jpeg') no-repeat center;
  background-size: cover;
}
.wrapper {
  text-align: center;
}
.wrapper button {
  width: 80%;
  padding: 20px;
  margin-top: 30px;
}

.dedication {
  margin-top: -5pt;
  color: var(--mouseGrey);
}
/** special input style for login **/
.input-box {
  margin-top: 20px;
  position: relative;
}
.input-box input {
  width: 80%;
  height: 80%;
  background: transparent;
  outline: none;
  border: 2px solid rgba(255, 255, 255, .2);
  color: #fff;
  padding: 20px 45px 20px 20px;
}
.input-box i {
  position: absolute;
  right: 12%;
  top: 50%;
  transform: translateY(-50%);
  font-size: var(--iconsFontSize);
}
input {
  font-size: var(--standardFontSize);
  border-radius: var(--formInputBorderRadius);
}
@media screen and (max-width: 750px){
  input {
    font-size: var(--standardFontSizeMobil);
  }
}

input:focus {
  border-color: var(--lightestGrey);
}

input::placeholder {
  color: var(--mouseGrey);
}

@media screen and (max-width: 900px){
  .wrapper button {
    padding: 10px;
    margin-top: 15px;
  }
}
</style>