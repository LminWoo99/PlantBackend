import React, { useState, useEffect } from "react";
import axios from "axios";
import "./App.css";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";

const NoticeWriteComponent = ({ onEditorChange, onSubmit }) => {
  const [desc, setDesc] = useState("");

  const handleChange = (value) => {
    setDesc(value);
    onEditorChange(value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    onSubmit();
  };

  return (
    <div className="notice-write-component">
      <div className="editor-wrapper">
        <ReactQuill value={desc} onChange={handleChange} />
      </div>
      <div className="button-wrapper">
        <button className="app-button" onClick={handleSubmit}>
          등록
        </button>
      </div>
    </div>
  );
};



const App = () => {
  const [tTitle, setTTitle] = useState("");
  const [tContent, setTContent] = useState("");

  const handleTTitleChange = (event) => {
    setTTitle(event.target.value);
  };

  const handleTContentChange = (value) => {
    setTContent(value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    if (!tTitle || !tContent) {
      return;
    }

    const newItem = {
      title: tTitle,
      content: tContent,
    };
    axios
      .post("/post", newItem)
      .then((response) => {
        // 처리 완료 후 작성한 글 초기화
        setTTitle("");
        setTContent("");
      })
      .catch((error) => console.log(error));
  };

  return (
    <div className="app-container">
      <h1 className="app-title">식구2</h1>
      <form onSubmit={handleSubmit} className="app-form">
        <select className="app-select">
          <option>게시판 종류</option>
          <option value="notice">정보게시판</option>
          <option value="free">자유게시판</option>
          <option value="alpha">플러스 알파</option>
        </select>
        <input
          type="text"
          value={tTitle}
          onChange={handleTTitleChange}
          placeholder="제목"
          className="app-input"
        />
        <NoticeWriteComponent
          onEditorChange={handleTContentChange}
          onSubmit={handleSubmit}
        />
      </form>
    </div>
  );
};


export default App;
