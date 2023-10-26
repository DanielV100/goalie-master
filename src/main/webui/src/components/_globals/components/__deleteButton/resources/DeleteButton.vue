<script setup>
function deleteDiv() {
  alert('hello');
}
</script>

<template>
  <div class="__delete_button">
    <button @click="deleteDiv()" class="deleteButton">-</button>
  </div>
</template>

<style scoped>
.__delete_button {
  width: 15%;
  margin-left: 8px;

}
button {
}
</style>