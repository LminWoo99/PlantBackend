import React, { useState, useEffect } from "react";
import axios from "axios";
import "./App.css";

const App = () => {
  const [items, setItems] = useState([]);
  const [tTitle, setTTitle] = useState("");
  const [tContent, setTContent] = useState("");
  const [csrfToken, setCsrfToken] = useState("");

  useEffect(() => {
    async function fetchData() {
      try {
        const response = await axios.get("/csrf-token", { withCredentials: true });
        const token = response.data.csrfToken;
        setCsrfToken(token);
      } catch (error) {
        console.log(error);
      }
    }

    fetchData();
  }, []);

  const handleTTitleChange = (event) => {
    setTTitle(event.target.value);
  };

  const handleTContentChange = (event) => {
    setTContent(event.target.value);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!tTitle || !tContent) {
      return;
    }

    const newItem = {
      title: tTitle,
      content: tContent
    };

    try {
      await axios.post("/post", newItem, {
        headers: {
          "X-CSRF-Token": csrfToken
        },
        withCredentials: true
      });

      setItems([...items, newItem]);
      setTTitle("");
      setTContent("");
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className="app-container">
      <h1 className="app-title">식구xdxdx</h1>
      <form onSubmit={handleSubmit} className="app-form">
        <select className="app-select">
          <option>게시판 종류</option>
          <option value="notice">정보게시판</option>
          <option value="free">자유게시판</option>
          <option value="free">플러스 알파</option>
        </select>
        <input
          type="text"
          value={tTitle}
          onChange={handleTTitleChange}
          placeholder="제목"
          className="app-input"
        />
        <textarea
          value={tContent}
          onChange={handleTContentChange}
          placeholder="내용"
          className="app-textarea"
        ></textarea>
        <button type="submit" className="app-button">
          등록
        </button>
      </form>
    </div>
  );
};

export default App;
