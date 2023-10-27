<script setup>
import EditExerciseForm from "@/components/_detailed/components/__addExerciseForm/ViewExerciseForm.vue";
const obj= [
  { noMat: '2', mat: 'Stangen' },
  { noMat: '9', mat: 'Hütchen' },
  { noMat: '3', mat: 'Bälle' },
  { noMat: '1', mat: 'Tor' }
]
</script>

<template>
  <div class="__accordion">
    <div class="table-container">
      <table>
        <tr>
          <td style="width: 50%">
            <label>Kategorie</label>
            <h2><slot></slot></h2>
          </td>
          <td class="after_td_one">
            <label>Dauer</label>
            <h2>00:00</h2>
          </td>
          <td class="after_td_one">
            <label>Intensität</label>
            <h2>2/5</h2>
          </td>
        </tr>
      </table>
      <hr>
      <EditExerciseForm title="dgdjsa" category="coordination" number-of-goalkeeper="2" duration="00:40" intensity="3" material="Stangen" material-count="2" :test-array="obj"></EditExerciseForm>
      <br>
      <hr>
      <button>+</button>
    </div>
  </div>
</template>

<style scoped>
.__accordion {
  width: 100%;
}

.table-container {
  overflow-x: auto;
}
.table-container table {
  width: 100%;
  border-collapse: collapse;
}
.after_td_one {
  padding-left: 20px;
}
.after_td_one h2 {
  color: var(--placeholderColor);
}
h2 {
  margin-top: 0;
}
button {
  width: 100%;
}


</style>
