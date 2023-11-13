# Daily Trivial

## Trabajo Realizado por Rodrigo de la Calle Alonso

Este trabajo consiste en un juego de preguntas tipo trivia diarias,
en el que el usuario deberá responder a 5 preguntas de diferentes categorias
y posteriormente podrá revisar sus estadísticas.

Para obtener las preguntas se ha utilizado la API de [Open Trivia DB](https://opentdb.com/).
Donde se realiza una unica llamada diaria para obtener las preguntas y estas se almacenaran en el dispositivo hasta finalizar el dia.

## Destacar que se han implementado todos los puntos obligatorios y algunos de los opcionales como:

✅ **Recuperar Contraseña:**
Se ha implementado la recuperación de contraseña mediante el envío de un correo electrónico.

✅ **Estadísticas:**
Gráficas de estadísticas de las preguntas respondidas por el usuario mediante la libreria [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)

✅ **Persistencia Local:**
Se ha implementado la persistencia local de las preguntas diarias, para que el usuario pueda volver a ver las preguntas que ha contestado y además evitar que haga trampas solicitando otras nuevas.

## Video Demostración
<iframe width="560" height="315" src="https://youtu.be/Hg-JeNQ1pY8" frameborder="0" allowfullscreen></iframe>
[![Enlace al video de Youtube](https://youtu.be/Hg-JeNQ1pY8)](https://youtu.be/Hg-JeNQ1pY8)

---

## Modelo

Para el modelo se han implementado cinco clases:

### Datos de Usuario

- **User:** Representa al usuario, contiene los datos del usuario y las estadísticas de las preguntas respondidas.
- **Record:** Representa al registro diario del número de preguntas acertadas, falladas o no contestadas.
- **Category:** Representa a una categoría de preguntas, contiene las estadísticas de esa categoría.
#### API

- **Trivia:** Representa el objeto que devuelve la llamada a la API, se guardan en un archivo json en local mediante la libreria [gson](https://www.javadoc.io/doc/com.google.code.gson/gson/2.8.5/com/google/gson/Gson.html).
- **Result:** Representa a una pregunta, contiene los datos de la pregunta y la respuesta correcta, entre otros.

## Funcionamiento

Cada día se comprueba si ya se ha realizado la partida diaria y en caso contrario se hace una llamada a la API para obetener las preguntas. Depués se guardan en local y se muestran para que el usuario responda. Una vez validadas, se actulizan los datos en Firestore hasta la próxima partida.

---

**Universidad Politécnica de Madrid**
**Master en Ingeniería Web**
**12/11/2023**