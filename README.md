# Magneto Service
Este proyecto tiene como objetivo cumplir los requerimientos solicitados en el examen MercadoLibre para ayudar a Magneto a contratar mutantes para luchar contra los X-Mens. Este proyecto ha sido desarrollado con IntelliJ en lenguaje Java 8 utilizando el framework [Spring boot](https://spring.io/projects/spring-boot) y ha sido desplegado en [Amazon Web Service](https://aws.amazon.com/es/)

El proyecto consta de 3 servicios que deben actuar conjuntamente para lograr cubrir las necesidades de Magneto.
Los servicios son:
* [Magneto-Service](https://github.com/hccr/magneto-service/), El cual expone un Endpoint para validar si un ADN es mutante o no.
* [Magneto-Processor](https://github.com/hccr/magneto-processor), Proyecto que esta encargado de almacenar en una base de datos los ADN consultados. Este proceso ocurre de manera asincrona.
* [Magneto-Statistics](https://github.com/hccr/magneto-statistics), Quien es el encargado de entretar las estadisticas sobre los ADN consultados.

## Supuestos considerados
Dentro del desarrollo del proyecto se consideraron supuestos, los cuales condicionan el funcionamiento del sistema.

* Las secuencias de ADN vienen en un arreglo de String y se podrán convertir en una matriz de NxN. El no cumplir ese requerimiento arrojará una excepción `NotNxNDnaFormatException`.
* Las secuencias de ADN solo podrán tener los caracteres `A,C,G,T`, si una secuencia contiene otro caracter se arrojará la excepción `NotAllowesCharException`
* La secuencia de ADN al ser convertida en matríz NxN debe tener un tamaño mayor igual que 4x4 y menor o igual que 64x64, de lo contrario se arrojará la excepción `DnaSizeException`

## Funcionamiento
Magneto-Service es un API Rest que consta principalmente de 2 rutas `/mutant` y `/stats` las cuales detallo a continuación.

### `/mutant/`
```
POST → /mutant/
{
“dna”:["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
}
```
Este método Post recibe dentro del cuerpo de la petición un objeto JSON con un atributo `dna`, el cual debe ser un arreglo de Strings. Tanto el largo del arreglo como el largo de cada String deben ser del mismo tamaño.
#### Respuesta
El método devolverá un código de respuesta `HTTP 200-OK` si es que el dna corresponde a un mutante. En caso de que el dna enviado sea de un humano, el API responderá con el código `HTTP 403-Forbidden`. Debido a que no se especificó dentro de los requerimientos, el método no devuelve contenido al ser llamado.
#### Ejemplo
```
curl -i -X POST http://YOUR-ENDPOINT/mutant \
  -H 'Content-Type: application/json' \
  -d '{
        "dna":["ATGCGA",
            "CAGTGC",
            "TTATTT",
            "AGACGG",
            "GCGTCA",
            "TCACTG"]
}'
```
#### Excepciones
El metodo tiene detección de excepciones las cuales pueden ocurrir por los siguientes motivos:
* Tamaño inadecuado de la cadena de dna
* Contiene letras no permitidas
* No es una matríz NxN
En todos estos casos se responde con el código `HTTP 404-Bad Request`con el mensaje correspondiente.


### `/stats`
```
GET → /stats
```
Este método Get entrega las estadísticas de consulta del servicio en formato JSON. Los campos entregados son los siguientes:
* `count_mutant_dna (int)`: Indica el número de adn mutantes verificados.
* `count_human_dna (int)`: Indica el número de adn humanos verificados.
* `ratio (double)`: Indica la relación mutante/humano.
#### Respuesta
El método devolverá un código de respuesta `HTTP 200-OK` con un objeto JSON dentro del cuerpo de la respuesta. `{"count_mutant_dna":40, "count_human_dna":100: "ratio":0.4}`

#### Ejemplo
```
curl -X GET http://YOUR-ENDPOINT/stats
```
