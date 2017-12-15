object Form1: TForm1
  Left = 221
  Top = 119
  Width = 1456
  Height = 826
  Align = alClient
  Caption = 'mainForm'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  OnCreate = FormCreate
  PixelsPerInch = 96
  TextHeight = 13
  object Panel1: TPanel
    Left = 0
    Top = 0
    Width = 1586
    Height = 445
    Align = alTop
    TabOrder = 0
    object Label1: TLabel
      Left = 300
      Top = 92
      Width = 105
      Height = 46
      Alignment = taCenter
      Caption = #25805#20316#21592
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -35
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
    end
    object operatorLb: TLabel
      Left = 414
      Top = 98
      Width = 303
      Height = 44
      AutoSize = False
      Color = clScrollBar
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -33
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentColor = False
      ParentFont = False
    end
    object Label6: TLabel
      Left = 15
      Top = 27
      Width = 70
      Height = 46
      Caption = #32447#21495
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -35
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
    end
    object Label7: TLabel
      Left = 302
      Top = 29
      Width = 70
      Height = 46
      Caption = #24037#21333
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -35
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
    end
    object Label8: TLabel
      Left = 18
      Top = 92
      Width = 140
      Height = 46
      Caption = #26495#38754#31867#22411
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -35
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
    end
    object typeLb: TLabel
      Left = 744
      Top = 11
      Width = 840
      Height = 208
      Alignment = taCenter
      AutoSize = False
      Caption = #25805#20316
      Color = clGreen
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindow
      Font.Height = -117
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentColor = False
      ParentFont = False
      WordWrap = True
    end
    object resultLb: TLabel
      Left = 744
      Top = 219
      Width = 839
      Height = 223
      Alignment = taCenter
      AutoSize = False
      Caption = #32467#26524
      Color = clGreen
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindow
      Font.Height = -117
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentColor = False
      ParentFont = False
      WordWrap = True
    end
    object materialNoLb: TLabel
      Left = 173
      Top = 304
      Width = 369
      Height = 52
      AutoSize = False
      Color = clSilver
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -40
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentColor = False
      ParentFont = False
    end
    object scanMaterialNoLb: TLabel
      Left = 173
      Top = 363
      Width = 367
      Height = 52
      AutoSize = False
      Color = clSilver
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -40
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentColor = False
      ParentFont = False
    end
    object Label5: TLabel
      Left = 23
      Top = 362
      Width = 140
      Height = 46
      Caption = #25195#25551#26009#21495
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -35
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
    end
    object Label4: TLabel
      Left = 22
      Top = 305
      Width = 140
      Height = 46
      Caption = #21407#22987#26009#21495
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -35
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
    end
    object lineseatLb: TLabel
      Left = 172
      Top = 186
      Width = 370
      Height = 52
      AutoSize = False
      Color = clSilver
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -40
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentColor = False
      ParentFont = False
    end
    object scanLineseatLb: TLabel
      Left = 173
      Top = 246
      Width = 369
      Height = 52
      AutoSize = False
      Color = clSilver
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -40
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentColor = False
      ParentFont = False
    end
    object Label3: TLabel
      Left = 23
      Top = 243
      Width = 140
      Height = 46
      Caption = #25195#25551#31449#20301
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -35
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
    end
    object Label2: TLabel
      Left = 22
      Top = 186
      Width = 140
      Height = 46
      Caption = #21407#22987#31449#20301
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -35
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
    end
    object lineCb: TComboBox
      Left = 100
      Top = 31
      Width = 190
      Height = 44
      Style = csDropDownList
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -27
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ItemHeight = 36
      ParentFont = False
      TabOrder = 0
      OnChange = lineCbChange
      OnDropDown = lineCbDropDown
      Items.Strings = (
        '301'
        '302'
        '303'
        '304'
        '305'
        '306'
        '307'
        '308')
    end
    object workOrderCb: TComboBox
      Left = 379
      Top = 31
      Width = 341
      Height = 44
      Style = csDropDownList
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -27
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ItemHeight = 36
      ParentFont = False
      TabOrder = 1
      OnChange = workOrderCbChange
      OnDropDown = workOrderCbDropDown
    end
    object boardTypeCb: TComboBox
      Left = 173
      Top = 96
      Width = 116
      Height = 44
      Style = csDropDownList
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -27
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ItemHeight = 36
      ParentFont = False
      TabOrder = 2
      OnChange = boardTypeCbChange
    end
  end
  object Panel2: TPanel
    Left = -6
    Top = 447
    Width = 1592
    Height = 343
    Caption = 'Panel2'
    TabOrder = 1
    object dataGrid: TDBGrid
      Left = 1
      Top = 4
      Width = 1590
      Height = 338
      Align = alBottom
      DataSource = ds
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = 60
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
      ReadOnly = True
      TabOrder = 0
      TitleFont.Charset = DEFAULT_CHARSET
      TitleFont.Color = clWindowText
      TitleFont.Height = -11
      TitleFont.Name = 'MS Sans Serif'
      TitleFont.Style = []
      OnDrawColumnCell = dataGridDrawColumnCell
      Columns = <
        item
          Alignment = taCenter
          Expanded = False
          FieldName = 'lineseat'
          Font.Charset = ANSI_CHARSET
          Font.Color = clBlack
          Font.Height = -55
          Font.Name = #24494#36719#38597#40657
          Font.Style = [fsBold]
          Title.Alignment = taCenter
          Title.Caption = #31449#20301
          Title.Font.Charset = ANSI_CHARSET
          Title.Font.Color = clWindowText
          Title.Font.Height = -33
          Title.Font.Name = #24494#36719#38597#40657
          Title.Font.Style = []
          Width = 200
          Visible = True
        end
        item
          Alignment = taCenter
          Expanded = False
          FieldName = 'store_issue_result'
          Font.Charset = ANSI_CHARSET
          Font.Color = clGreen
          Font.Height = -55
          Font.Name = #24494#36719#38597#40657
          Font.Style = [fsBold]
          Title.Alignment = taCenter
          Title.Caption = #21457#26009
          Title.Font.Charset = ANSI_CHARSET
          Title.Font.Color = clWindowText
          Title.Font.Height = -33
          Title.Font.Name = #24494#36719#38597#40657
          Title.Font.Style = []
          Width = 204
          Visible = True
        end
        item
          Alignment = taCenter
          Expanded = False
          FieldName = 'feed_result'
          Font.Charset = ANSI_CHARSET
          Font.Color = clGreen
          Font.Height = -55
          Font.Name = #24494#36719#38597#40657
          Font.Style = [fsBold]
          Title.Alignment = taCenter
          Title.Caption = #19978#26009
          Title.Font.Charset = ANSI_CHARSET
          Title.Font.Color = clWindowText
          Title.Font.Height = -33
          Title.Font.Name = #24494#36719#38597#40657
          Title.Font.Style = []
          Width = 197
          Visible = True
        end
        item
          Alignment = taCenter
          Expanded = False
          FieldName = 'change_result'
          Font.Charset = ANSI_CHARSET
          Font.Color = clGreen
          Font.Height = -55
          Font.Name = #24494#36719#38597#40657
          Font.Style = [fsBold]
          Title.Alignment = taCenter
          Title.Caption = #25442#26009#13#10
          Title.Font.Charset = ANSI_CHARSET
          Title.Font.Color = clWindowText
          Title.Font.Height = -33
          Title.Font.Name = #24494#36719#38597#40657
          Title.Font.Style = []
          Width = 213
          Visible = True
        end
        item
          Alignment = taCenter
          Expanded = False
          FieldName = 'check_result'
          Font.Charset = ANSI_CHARSET
          Font.Color = clGreen
          Font.Height = -55
          Font.Name = #24494#36719#38597#40657
          Font.Style = [fsBold]
          Title.Alignment = taCenter
          Title.Caption = #26680#26009
          Title.Font.Charset = ANSI_CHARSET
          Title.Font.Color = clWindowText
          Title.Font.Height = -33
          Title.Font.Name = #24494#36719#38597#40657
          Title.Font.Style = []
          Width = 211
          Visible = True
        end
        item
          Alignment = taCenter
          Expanded = False
          FieldName = 'check_all_result'
          Font.Charset = ANSI_CHARSET
          Font.Color = clGreen
          Font.Height = -55
          Font.Name = #24494#36719#38597#40657
          Font.Style = [fsBold]
          Title.Alignment = taCenter
          Title.Caption = #20840#26816
          Title.Font.Charset = ANSI_CHARSET
          Title.Font.Color = clWindowText
          Title.Font.Height = -33
          Title.Font.Name = #24494#36719#38597#40657
          Title.Font.Style = []
          Width = 223
          Visible = True
        end
        item
          Alignment = taCenter
          Expanded = False
          FieldName = 'first_check_all_result'
          Font.Charset = ANSI_CHARSET
          Font.Color = clGreen
          Font.Height = -55
          Font.Name = #24494#36719#38597#40657
          Font.Style = [fsBold]
          Title.Alignment = taCenter
          Title.Caption = #39318#26816
          Title.Font.Charset = ANSI_CHARSET
          Title.Font.Color = clWindowText
          Title.Font.Height = -33
          Title.Font.Name = #24494#36719#38597#40657
          Title.Font.Style = []
          Width = 240
          Visible = True
        end>
    end
  end
  object DBConnection: TADOConnection
    ConnectionString = 
      'Provider=MSDASQL.1;Persist Security Info=False;Data Source=smt_e' +
      'ps'
    LoginPrompt = False
    Provider = 'MSDASQL.1'
    Left = 626
    Top = 206
  end
  object selectQry: TADOQuery
    Connection = DBConnection
    Parameters = <>
    Prepared = True
    Left = 574
    Top = 200
  end
  object mainQry: TADOQuery
    Connection = DBConnection
    Parameters = <>
    Prepared = True
    Left = 669
    Top = 252
  end
  object operatorQry: TADOQuery
    Connection = DBConnection
    Parameters = <>
    Prepared = True
    Left = 620
    Top = 267
  end
  object ds: TDataSource
    DataSet = mainQry
    Left = 570
    Top = 257
  end
  object refreshTimer: TTimer
    Interval = 3000
    OnTimer = refreshTimerTimer
    Left = 682
    Top = 210
  end
end
