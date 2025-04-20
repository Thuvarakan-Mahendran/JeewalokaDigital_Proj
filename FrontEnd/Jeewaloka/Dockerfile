# Step 1: Build the app
FROM node:18 as build

WORKDIR /app
COPY . .

# Accept build-time environment variable for API base URL
ARG REACT_APP_API_BASE_URL
ARG VITE_API_BASE_URL
ENV REACT_APP_API_BASE_URL=$REACT_APP_API_BASE_URL
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL

# Install and build the React app
RUN npm install
RUN npm run build

# Step 2: Serve using nginx
FROM nginx:alpine

# Remove default nginx website
RUN rm -rf /usr/share/nginx/html/*

# Copy built assets to nginx public folder
COPY --from=build /app/dist /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]