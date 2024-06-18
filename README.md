# LAB | Java Excepciones y Pruebas (Testing)

## Introducción

Acabamos de aprender cómo crear excepciones personalizadas, cómo manejarlas en controladores y cómo usar MockMVC para probar los controladores, así que practiquemos un poco más.

<br>

## Requisitos

1. Haz un fork de este repositorio.
2. Clona este repositorio.
3. Añade a tu instructor y a los calificadores de la clase como colaboradores de tu repositorio. Si no estás seguro de quiénes son los calificadores de tu clase, pregunta a tu instructor o consulta la presentación del primer día.
4. En el repositorio, crea un proyecto de Java y añade el código para las siguientes tareas.

## Entrega

Una vez que termines la tarea, envía un enlace URL a tu repositorio o tu solicitud de extracción en el campo de abajo.

<br>

## Configuración

Combina los dos laboratorios `4.02` y `4.04` y luego copia el código a tu nuevo repositorio.

<br>

## Especificaciones

1. Prueba todas las rutas disponibles utilizando `MockMVC`.

<br>

## Consejos

Para probar las rutas, puedes seguir estos pasos:

1. Comienza importando las dependencias necesarias para la integración de tests, como `@SpringBootTest` de Spring Boot, `MockMvc` y `WebApplicationContext`.
2. En tu clase de test, usa la anotación `@SpringBootTest` para configurar el entorno de test y crea un objeto de `WebApplicationContext` inyectándolo.
3. A continuación, crea un objeto de `MockMvc` utilizando `MockMvcBuilders.webAppContextSetup(webApplicationContext)`.
4. Usa el objeto `MockMvc` para realizar solicitudes GET, POST y PUT a las diferentes rutas en tu aplicación.
5. Para los casos positivos, usa el método `.andExpect(status().isOk())` para asegurarte de que se devuelva el código de estado adecuado (200 OK) en la respuesta. También puedes usar `.andExpect(content().json("json esperado"))` para verificar si el json devuelto coincide con lo que esperas.
6. Para los casos negativos, usa el método `.andExpect(status().is(400))` para verificar que el código de estado devuelto sea 400 Bad Request. También puedes usar `.andExpect(status().reason(containsString("Bad Request")))` para verificar si el cuerpo de la respuesta contiene el mensaje de error adecuado.
7. Para manejar las variables de ruta y los parámetros de consulta, puedes usar el método `.param("nombreParam", "valorParam")` para agregarlos a la solicitud antes de realizarla.
8. Repite los pasos 4-7 para todas las rutas GET, POST y PUT en tu aplicación, incluyendo las rutas de los Laboratorios 4.02 y 4.04.
9. Ejecuta tus tests y verifica que todas ellas pasen.

Es importante tener en cuenta que los tests deben estar aisladas y no deben tener efectos secundarios en otros tests, por lo que asegúrate de limpiar cualquier dato creado durante el test.

<br>