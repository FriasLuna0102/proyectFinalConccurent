## Descripción del diseño del sistema.

### El proyecto es una simulación de una intersección de tráfico que incluye normal y de emergencia, y controla los semáforos para dirigir el tráfico.

### Paquete org.example.projectfinal.enumeraciones: Este paquete contiene las enumeraciones utilizadas en el proyecto, que definen los diferentes estados y tipos posibles para los componentes de la simulación (vehículos y semáforos).

•	Dirección: Enumera las direcciones posibles para los vehículos (DERECHA, IZQUIERDA, ARRIBA, ABAJO).
•	EstadoSemaforo: Enumera los posibles estados de los semáforos (ROJO, AMARILLO, VERDE).
•	EstadoVehiculo: Enumera los posibles estados de los vehículos (ESPERANDO, EN_MOVIMIENTO, DETENIDO).
•	TipoVehiculo: Enumera los tipos de vehículos (NORMAL, EMERGENCIA).

Paquete org.example.projectfinal.modelo: Este paquete contiene las clases principales que modelan los elementos de la simulación.

•	Autopista: Representa una autopista con un identificador único y una lista de calles que la componen.
•	Calle: Representa una calle con un identificador único, un número de carriles y una lista de vehículos.
•	Intersección: Representa una intersección con un identificador, una colección de vehículos organizados por dirección, y semáforos para cada dirección. Incluye lógica para controlar los semáforos y gestionar los cambios de estado.
•	Semáforo: Representa un semáforo con un identificador, estado actual, tiempos para cada estado (verde, rojo, amarillo) y la lógica para actualizar el estado basado en el tiempo transcurrido.
•	Vehículo: Representa un vehículo con un identificador, tipo, dirección, estado, posición, velocidad y lógica para detenerse y reanudar el movimiento, además de métodos para dibujarlo y moverlo.

Paquete org.example.projectfinal: Este paquete contiene la clase principal de la aplicación y el controlador de la interfaz gráfica.

•	HelloApplication: La clase principal que extiende Application de JavaFX y maneja la interfaz gráfica y la lógica de simulación.
•	HelloController: Controlador para manejar la interacción con la interfaz gráfica.

Archivo de Módulo module-info.java: Define las dependencias y los módulos requeridos por la aplicación.

•	module org.example.projectfinal: Incluye las dependencias necesarias para JavaFX y otros módulos utilizados.

Explicación de los Algoritmos de Control.

•	Control de Semáforos: El control de los semáforos se realiza mediante la clase Intersección, que contiene un mapa de semáforos por dirección y una dirección actual en verde (direccionVerde). El método controlarSemaforos() actualiza el estado del semáforo actual y cambia la dirección en verde cuando el tiempo asignado a cada estado ha transcurrido.
•	Movimiento y Dibujo de Vehículos: Los vehículos se mueven y se dibujan en el contexto gráfico de la clase HelloApplication. El método moverYdibujarVehiculos(GraphicsContext gc) controla el movimiento de los vehículos, deteniéndolos o reanudándolos según el estado de los semáforos y la proximidad de otros vehículos. Los vehículos de emergencia tienen prioridad y pueden pasar incluso si el semáforo está en rojo.

Instrucciones para Ejecutar la Aplicación.

## Prerrequisitos: 
•	Java 8 o superior
•	JavaFX
•	Un IDE compatible (por ejemplo, IntelliJ IDEA, Eclipse)

## Configuración del Proyecto:

•	Importa el proyecto en tu IDE.
•	Asegurarse de que todas las dependencias necesarias están incluidas y configuradas en el archivo module-info.java.

## Ejecución:
•	Ejecutar la clase HelloApplication como una aplicación Java.
•	En la interfaz gráfica, seleccionar el escenario y hacer clic en "Iniciar Simulación".
•	Agregar vehículos usando el combo box de tipo de vehículo y dirección, y hacer clic en agregar Vehículo.


## Resultados de pruebas y evaluación del sistema.

El sistema de simulación de tráfico ha demostrado ser efectivo y funcional a través de una serie de pruebas rigurosas. Las pruebas de funcionalidad confirmaron que la simulación se inicia correctamente y los semáforos cambian de estado según lo esperado, sin errores. Los vehículos se agregan y se mueven conforme a la lógica definida, y los vehículos de emergencia tienen prioridad sobre los normales y los semáforos, comportándose adecuadamente incluso cuando los semáforos están en rojo. Estas pruebas validan que los componentes críticos del sistema funcionan de manera coherente y conforme a las expectativas.

La evaluación del sistema también destacó su desempeño, precisión y usabilidad. La simulación se mantiene fluida y la interfaz responde adecuadamente, incluso con múltiples vehículos en escena, lo que indica un uso eficiente de los recursos del sistema. Los algoritmos de control de semáforos y movimiento de vehículos mostraron ser precisos y efectivos, asegurando un comportamiento realista en la simulación. Además, la interfaz gráfica es intuitiva y fácil de usar, permitiendo a los usuarios interactuar con la aplicación sin dificultades. En resumen, el sistema cumple con sus objetivos, proporcionando una simulación de tráfico precisa y una experiencia de usuario positiva.

