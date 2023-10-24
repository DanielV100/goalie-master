<script setup>
function homeIconClicked() {
  returnToOverviewPage();
}

function returnToOverviewPage() {
  /**
   * ToDo: Implement logic to return to overview pafe
   */
}


</script>

<template>
  <div class="__current_page_indicator">
    <i @click="homeIconClicked" class="bx bx-home-alt-2"></i>
    <i class="bx bx-chevron-right"></i>
    <p><slot></slot></p>
  </div>
</template>

<style>
.__current_page_indicator {
  margin-top: 8px;
  margin-left: 12px;
  display: flex;
  align-items: center;
  color: var(--deepOceanBlue);
}
.__current_page_indicator i {
  display: inline-block;
  font-size: var(--standardFontSize);
  font-weight: 100;
}
.__current_page_indicator i:hover {
  color: black;
  font-weight: bold;
}
.__current_page_indicator p {
  display: inline-block;
  font-size: var(--standardFontSize);
}
</style>