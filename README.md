Procesamiento Avanzado de Imagenes

He cambiado ligeramente el api de los metodos del trabajo para orientarlos un poco más al estilo de programacion en java. La idea principal es que los objetos del tipo PGMImage implementen metodos con sus operaciones. Cuando esta operacion involucra otra imagen es pasada como parámetro.

La clase PGMImage contiene la representacion de la imagen y metodos para invocar las operaciones. Las clases PGMReader y PGMWriter son clases de ayuda para poder leer y escribir imagenes. La clase PGMReaderNio es para leer un archivo utilizando las funcionalidades de java NIO, pero no he logrado que funcione correctamente.

El paquete connectivity tiene una interfaz y una clase con la implementacion de 8 connectivity. La idea detrás de esto es que si tuviera que cambiar los algoritmos a 4 connectivity pueda hacerlo creando una clase que implemente esta interfaz y el método. La clase 8 connectivity (que en realidad implementa conectividad con las diagonales sin importar el tamaño) podria hacerse más optimamente revisando los limites de las iteraciones contra el inicio y fin de la imagen en vez de capturando la excepción.

El paquete operator implementa operaciones comunes para poder hacer los algoritmos de recorrido de PGMImage más genericos. Se debería separar la interfaz IOperator en dos según sea una operación de un pixel o dos.


