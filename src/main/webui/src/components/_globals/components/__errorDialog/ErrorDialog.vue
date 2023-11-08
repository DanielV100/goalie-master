<script setup>
import {onMounted} from "vue";
import * as LocalConfig from './resources/errorDialogConfig.js';

let dialog;
onMounted(() => {
  dialog = document.querySelector('dialog');
});

function closeButtonClicked() {
  dialog.close();
}


</script>

<template>
  <div class="error-dialog-container">
    <dialog id="errorDialog">
      <div class="dialog-content">
        <h2>{{ LocalConfig.ERROR_HEADLINE }}</h2>
        <p><slot></slot></p>
        <button class="deleteButton" @click="closeButtonClicked">&#10006;</button>
      </div>
    </dialog>
  </div>
</template>

<style scoped>
.error-dialog-container {

}
h2 {
  color: white;
}
dialog {
  background: #ce5656;
  color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 100%;
  padding: 16px;
  border: none;
}

.dialog-content {
  text-align: center;
}

.dialog-content h2 {
  margin: 0 0 16px;
}

.dialog-content p {
  margin: 0 0 16px;
  font-size: 1rem;
}

button {
  width: 60%;
}

</style>