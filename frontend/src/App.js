import React, { useState, useEffect } from "react";
import axios from "axios";
import "./App.css";

const App = () => {
  const [items, setItems] = useState([]);
  const [tTitle, setTTitle] = useState("");
  const [tContent, setTContent] = useState("");

  useEffect(() => {
    axios
      .get("/post")
      .then((response) => setItems(response.data))
      .catch((error) => console.log(error));
  }, []);

  const [textareaHeight, setTextareaHeight] = useState({
    row: 1,
    lineBreak: {},
  });

  const handleTTitleChange = (event) => {
    setTTitle(event.target.value);
  };

  const handleTContentChange = (event) => {
    setTContent(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    if (!tTitle || !tContent) {
      return;
    }

    const newItem = {
      title: tTitle,
      content: tContent
    };
    axios
      .post("/post", newItem)
      .then((response) =>
        setItems([...items, response.data.tradeBoardDto])
      )
      .catch((error) => console.log(error));
    setTTitle("");
    setTContent("");
  };

  const handleDelete = (id) => {
    axios
      .delete(`/post/${id}`)
      .then((response) =>
        setItems(items.filter((item) => item.id !== id))
      )
      .catch((error) => console.log(error));
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
          등록d
        </button>
      </form>
    </div>
  );
};

export default App;