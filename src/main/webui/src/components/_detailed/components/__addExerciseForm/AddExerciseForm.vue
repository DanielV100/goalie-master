<script setup>
import * as LocalConfig from './resources/addExerciseFormConfig.js';
import CurrentPageIndicator from "@/components/_globals/components/__currentPageIndicator/CurrentPageIndicator.vue";
</script>

<template>
  <div class="__add_exercise_form">
    <CurrentPageIndicator>{{ LocalConfig.CURRENT_PAGE }}</CurrentPageIndicator>
    <div class="wrapper">
      <form @submit="onSubmitClick" class="form">
        <div class="column">
          <div class="input-box">
            <label>{{ LocalConfig.FORM_TITLE_LABEL }}</label>
            <input
                id="firstname"
                type="text"
                placeholder="Hechten II"
                required
            />
          </div>
          <div class="input-box">
            <label>{{ LocalConfig.FORM_CATEGORY_LABEL }}</label>
            <select name="cars" id="cars">
              <option value="others">Sonstige</option>
              <optgroup label="Aufwärmen">
                <option value="coordination">{{ LocalConfig.CATEGORY_WARM_UP_COORDINATION }}</option>
                <option value="balance_ability">{{ LocalConfig.CATEGORY_WARM_UP_BALANCE_ABILITY }}</option>
                <option value="balance_ability">{{ LocalConfig.CATEGORY_WARM_UP_JUMP_COORDINATION }}</option>
              </optgroup>
              <optgroup label="Hauptteil">
                <option value="1vs1">{{ LocalConfig.CATEGORY_MAIN_1vs1 }}</option>
                <option value="pike">{{ LocalConfig.CATEGORY_MAIN_PIKE }}</option>
              </optgroup>
              <optgroup label="Abschluss">
                <option value="mercedes">Mercedes</option>
                <option value="audi">Audi</option>
              </optgroup>
            </select>
          </div>
          <div class="input-box">
            <label>{{ LocalConfig.FORM_NUMBER_OF_GOALKEEPER_LABEL }}</label>
            <input
                type="number"
                placeholder="2"
                required
            />
          </div>
        </div>
        <div class="column">
          <div class="input-box">
            <label>{{ LocalConfig.FORM_DURATION_LABEL }}</label>
            <input
                onfocus="(this.type = 'time')"
                id="lastname"
                type="text"
                placeholder="00:15"
                required
            />
          </div>
          <div class="input-box">
            <label>{{ LocalConfig.FORM_INTENSITY_LABEL }}</label>
            <input
                type="number"
                min="0"
                max="5"
                placeholder="0 (gering) - 5 (hoch)"
                required
            />
          </div>
          <div class="input-box" id="multi">
            <div id="materialcontainer">
              <label style="width: 100%">Materialien</label>
              <div id="materialelement" style="display: flex">
                <input class="input" type="number" style="width: 20%; margin-right: 8pt" placeholder="0" />
                <input
                    @input="validateMandantoryFieldsFilled"
                    id="lastname"
                    type="text"
                    style="width: 80%"
                    placeholder="Stangen"
                    list="materials"
                    required
                />
                <datalist id="materials">
                  <option>Stangen</option>
                  <option>Hütchen</option>
                  <option>Bälle</option>
                  <option>Pylonen</option>
                </datalist>
              </div>
            </div>
            <button type="button" @click="addMaterial">+</button>
          </div>
        </div>
        <br />


      </form>
      <div id="describtionContainer">
        <form @submit="onSubmitClick" class="form" id="form">
          <div id="describtion">
            <h2>Schritt 1</h2>
            <textarea class="input" id="textTest" rows="3"></textarea>
          </div>

        </form>
      </div>
      <button type="button" @click="cloneExistingFields($event, 'describtionContainer', 'describtion')">+</button>
      <br />


      <br>
      <div>
        <div>
          <input @input="check" type="checkbox" name="testCheck">
          <label for="testCheck">Skizze hinzufügen</label>
        </div>

      </div>
      <div v-if="addDrawing" id="soccerFieldContainer">
        <SoccerField />
      </div>
      <br>


      <div>
        <div>
          <input @input="checkNotes" type="checkbox" name="testCheck">
          <label for="testCheck">Noziz hinzufügen</label>
        </div>
      </div>
      <div v-if="addNote">
        <h2>Schritt 1</h2>
        <textarea class="input" id="textTest" rows="3"></textarea>
      </div>
    </div>

  </div>
</template>

<style scoped>
.__add_exercise_form {
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

hr + p {
  font-size: 80pt;
  color: black;
}
#soccerFieldContainer {
  display: grid;
  cursor: crosshair;
  padding: 0;
}

#multi button {
  margin-top: 10pt;
}










</style>