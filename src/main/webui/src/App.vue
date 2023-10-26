<script setup>
import LoginForm from "@/components/_login/components/__login_form/LoginForm.vue";
import Header from "@/components/_globals/components/__header/Header.vue";
import Overview from "@/components/_menu/components/__overview/Overview.vue";
import AddGoalkeeperForm from "@/components/_detailed/components/__addGoalkeeperForm/AddGoalkeeperForm.vue";
import {computed, ref} from "vue";

//defining the routes to the other pages
const isShowingHeader = ref(false);

const ROUTES = {
  '/': LoginForm,
  '/menu': Overview,
  '/add-goalkeeper': AddGoalkeeperForm
};

const currentPath = ref(window.location.hash);
window.addEventListener('hashchange', () => {
  currentPath.value = window.location.hash;
});

const currentView = computed(() => {
  let current = ROUTES[currentPath.value.slice(1) || '/'];
  //header shouldn't be shown in login page
  if(current.__name !== 'LoginForm') {
    isShowingHeader.value = true;
  } else {
    isShowingHeader.value = false;
  }
  return  current || NotFound;
});

</script>

<template>
  <Header v-if="isShowingHeader"/>
  <component :is="currentView" />
</template>

<style>

</style>