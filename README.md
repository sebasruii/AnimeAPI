# AnimeAPI: Sistema de reseñas de anime
La idea consiste en una API que permite dejar reseñas sobre un recurso a elegir (lugares, pelis, libros, etc..).
El usuario podrá:
- consultar los animes disponibles
- consultar las reseñas de un anime en particular
- consultar los animes mediante algún filtro (ej: los 10 mejor valorados)
- crear un nuevo anime con sus respectivos metadatos
- crear una reseña sobre un anime, dando una valoración numérica y un comentario
- actualizar los atributos de un anime
- actualizar los valores de una reseña
- eliminar una reseña
- eliminar un anime

## 1. Recursos
El proyecto cuenta con dos recursos, cuyas propiedades se detallan a continuación:
### Anime
- Título
- Fecha de estreno
- Número de temporadas
- Número de capítulos
- Serie o película
- Puntuación media (a partir de valoraciones)
### Reseña
- Usuario
- Puntuación (1-5)
- Fecha
- Comentario (400 caracteres)


## 2. Endpoints
### /animes
#### GET
Devuelve un array con los datos de animes disponibles.
Soporta parámetros para realizar filtrado por parámetros como year, type (serie o film) y title. También soporta ordenación a través de valoración media (order) tanto ascendente como descendente (order-). Los resultados devueltos permiten ser limitados (limit=)  paginados (offset=).

### /animes/{animeId}
#### GET
Devuelve los datos de el anime con la id contenida en el path.

### /animes/reviews
#### GET
Devuelve un array con los datos de las reseñas publicadas.
Requiere el parámetro de filtrado user, que devuelve todas las reseñas publicadas por un usuario concreto. Tambien soporta el parámetro year.

#### POST
Crea una nueva reseña con los datos pasados en el cuerpo de la petición.

### /animes/reviews/{reviewId}
#### PUT
Actualiza los datos de la reseña con la id contenida en el path

#### DELETE
Elimina la reseña con la id contenida en el parámetro

### /animes/reviews/{animeId}
#### GET
Devuelve todas las reseñas del anime con la id contenida en el path
