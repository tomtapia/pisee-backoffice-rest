# Instalación Proyecto REST

<!-- TOC -->

- [Instalación Proyecto REST](#instalación-proyecto-rest)
  - [Requisitos](#requisitos)
  - [Clonar e Instalar en ambiente local](#clonar-e-instalar-en-ambiente-local)
  - [Proyecto](#proyecto)

<!-- /TOC -->

## Requisitos

- Java JDK.
- Apache ANT.
- Maven.
- Configuración del repositorio jboss en maven, como se explica en el readme del proyecto `Pisee-Backoffice`.

## Clonar e Instalar en ambiente local

1. Clonar el proyecto git:

```bash
 git clone git@github.com:e-gob/pisee-backoffice-rest.git
```

1. Importar en JBoss Developer como `Maven Project`.

1. Copiar los archivos de configuración de las integraciones REST desde el directorio `PiseeConf/rest_services` del proyecto `Pisee` en el directorio

```bash
$JBOSS_HOME/server/<INSTANCIA>/deploy/base/piseeConf/
```

## Proyecto

Este proyecto se creo con la finalidad de exponer los servicios de la PISEE (SOAP) en formato REST. Disminuyendo asi las barreras para que las instituciones se integren a la nueva normativa de `Interoperabilidad`.
