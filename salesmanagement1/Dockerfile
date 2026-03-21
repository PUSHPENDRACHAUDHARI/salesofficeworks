# Step 1: Build Angular
FROM node:18 AS build

WORKDIR /app
COPY . .
RUN npm install
RUN npm run build

# Step 2: Serve using Nginx
FROM nginx:alpine

COPY --from=build /app/dist /usr/share/nginx/html

# custom nginx config
COPY nginx/default.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
