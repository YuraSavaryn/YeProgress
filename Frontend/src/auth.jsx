import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";
import { getAuth } from "firebase/auth";

const firebaseConfig = {
  apiKey: "AIzaSyBIohtPpkZTL7OfHuqA2MxJV6QOltIKneo",
  authDomain: "yeprogress-916e3.firebaseapp.com",
  projectId: "yeprogress-916e3",
  storageBucket: "yeprogress-916e3.firebasestorage.app",
  messagingSenderId: "313903210286",
  appId: "1:313903210286:web:0f73d14222e75ecc2be4bf",
  measurementId: "G-6653CZM61E"
};

const app = initializeApp(firebaseConfig);
export const db = getFirestore(app)
const auth = getAuth(app);

export default auth;