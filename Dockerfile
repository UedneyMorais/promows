# Estágio de Build
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# Instala curl e ca-certificates para baixar o Maven
RUN apt-get update && \
    apt-get install -y curl ca-certificates --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

# Define a versão do Maven
ARG MAVEN_VERSION=3.9.6

# Baixa, extrai e configura o Maven
RUN curl -sSLO https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/${MAVEN_VERSION}/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    tar -xzf apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt && \
    rm apache-maven-${MAVEN_VERSION}-bin.tar.gz

# Adiciona o Maven ao PATH do sistema
ENV PATH="/opt/apache-maven-${MAVEN_VERSION}/bin:${PATH}"

# Copia todos os arquivos do contexto de build (onde está o Dockerfile, ou seja, a raiz de PROMOWS)
# para o diretório de trabalho /app dentro do contêiner.
# Isso garante que o pom.xml e a pasta src estejam no lugar certo.
COPY . .

# Baixa as dependências do Maven (aproveita o cache do Docker).
# Se as dependências no pom.xml não mudarem, essa etapa será rápida em builds futuros.
RUN mvn dependency:go-offline -B

# Compila a aplicação e gera o JAR.
# IMPORTANTE: Removidas as flags '-pl promows -am' pois não são necessárias para um módulo único.
RUN mvn clean install -DskipTests

# Estágio de Execução (Runtime)
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copia apenas o JAR compilado do estágio 'builder' para o estágio de execução.
# Como é um projeto de módulo único, o JAR estará diretamente em /app/target.
COPY --from=builder /app/target/*.jar app.jar

# Copia o script de entrypoint
COPY entrypoint.sh /app/entrypoint.sh

# Dá permissão de execução ao script
RUN chmod +x /app/entrypoint.sh

# Expõe a porta que sua aplicação Java usa.
EXPOSE 9090

# Define o comando para rodar a aplicação JAR.
ENTRYPOINT ["java", "-jar", "app.jar"]