
<script setup>
/**
 * This 404-not found page is shown, when user enters an url, which is not
 * connected to a page or a component.
 * @author daniel
 */


/**** IMPORTS ****/
/**** CONFIGS ****/
import * as LocalConfig from './resources/notFoundConfig';
import * as GlobalConfig from '@/globals/gloablConfig.js';

/**** CLICK-HANDLERS ****/
function buttonGoToLoginClicked() {
  GlobalConfig.changeWindowToTargetRoute('LoginForm');
}

</script>

<template>
  <div class="__not_found">
    <h1>{{ LocalConfig.NOT_FOUND_HEADING }}</h1>
    <h2>{{ LocalConfig.NOT_FOUND_SUB_HEADING }}</h2>
    <p>{{ LocalConfig.NOT_FOUND_TEXT }}</p>
    <button @click="buttonGoToLoginClicked">{{ LocalConfig.NOT_FOUND_BUTTON }}</button>
  </div>
</template>

<style scoped>
.__not_found {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  text-align: center;
  color: #333;
}

.__not_found h1 {
  font-size: 3rem;
  margin-bottom: 0.5rem;
}

.__not_found h2 {
  font-size: 2rem;
  margin-bottom: 1rem;
}

.__not_found p {
  margin-bottom: 2rem;
  color: #666;
}

.__not_found button {
  width: 50%;
}
</style>
