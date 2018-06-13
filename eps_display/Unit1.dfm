object Form1: TForm1
  Left = 185
  Top = 127
  Width = 1730
  Height = 874
  Align = alClient
  Caption = #20135#32447#23454#26102#30417#25511' - V1.11.4'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  WindowState = wsMaximized
  OnCreate = FormCreate
  OnDestroy = FormDestroy
  PixelsPerInch = 96
  TextHeight = 13
  object Panel1: TPanel
    Left = 0
    Top = 0
    Width = 1697
    Height = 1017
    Align = alTop
    TabOrder = 0
    object operatorNameLb: TLabel
      Left = 431
      Top = 20
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
      Left = 544
      Top = 21
      Width = 177
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
    object lineLb: TLabel
      Left = 19
      Top = 19
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
    object workOrderLb: TLabel
      Left = 18
      Top = 75
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
    object board_tybeLb: TLabel
      Left = 19
      Top = 133
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
    object materialNoLb: TLabel
      Left = 174
      Top = 300
      Width = 547
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
      Left = 174
      Top = 357
      Width = 547
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
    object scanMaterialNoNameLb: TLabel
      Left = 19
      Top = 360
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
    object materialNoNameLb: TLabel
      Left = 19
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
      Left = 174
      Top = 186
      Width = 547
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
      Left = 174
      Top = 243
      Width = 547
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
    object scanLineSeatNameLb: TLabel
      Left = 19
      Top = 246
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
    object lineSeatNameLb: TLabel
      Left = 19
      Top = 189
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
    object typeLb: TLabel
      Left = 744
      Top = 9
      Width = 682
      Height = 208
      Alignment = taCenter
      AutoSize = False
      Caption = #25805#20316
      Color = clGreen
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindow
      Font.Height = -160
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentColor = False
      ParentFont = False
      WordWrap = True
    end
    object resultLb: TLabel
      Left = 744
      Top = 210
      Width = 682
      Height = 223
      Alignment = taCenter
      AutoSize = False
      Caption = #32467#26524
      Color = clGreen
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindow
      Font.Height = -160
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentColor = False
      ParentFont = False
      WordWrap = True
    end
    object lineCb: TComboBox
      Left = 96
      Top = 20
      Width = 241
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
      Left = 96
      Top = 75
      Width = 625
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
      Left = 171
      Top = 133
      Width = 166
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
    object Button1: TButton
      Left = 568
      Top = 128
      Width = 153
      Height = 49
      Caption = #37325#32622#29366#24577
      Font.Charset = ANSI_CHARSET
      Font.Color = clRed
      Font.Height = -29
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
      TabOrder = 3
      OnClick = Button1Click
    end
  end
  object Panel2: TPanel
    Left = -14
    Top = 448
    Width = 1592
    Height = 406
    Caption = 'Panel2'
    TabOrder = 1
    object dataGrid: TDBGrid
      Left = 1
      Top = 0
      Width = 1590
      Height = 405
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
          Width = 171
          Visible = True
        end>
    end
  end
  object DBConnection: TADOConnection
    ConnectionString = 
      'Provider=MSDASQL.1;Persist Security Info=False;Data Source=smt_e' +
      'ps;'
    LoginPrompt = False
    Provider = 'MSDASQL.1'
    Left = 566
    Top = 8
  end
  object selectQry: TADOQuery
    Connection = DBConnection
    Parameters = <>
    Prepared = True
    Left = 530
    Top = 10
  end
  object mainQry: TADOQuery
    Connection = DBConnection
    Parameters = <>
    Prepared = True
    Left = 422
    Top = 7
  end
  object operatorQry: TADOQuery
    Connection = DBConnection
    Parameters = <>
    Prepared = True
    Left = 458
    Top = 7
  end
  object ds: TDataSource
    DataSet = mainQry
    Left = 496
    Top = 5
  end
  object refreshTimer: TTimer
    Interval = 3000
    OnTimer = refreshTimerTimer
    Left = 606
    Top = 8
  end
  object updateQry: TADOQuery
    Connection = DBConnection
    Parameters = <>
    Left = 649
    Top = 6
  end
  object secondaryQry: TADOQuery
    Connection = DBConnection
    DataSource = ds
    Parameters = <>
    Left = 688
    Top = 8
  end
  object resetQry: TADOQuery
    Connection = DBConnection
    Parameters = <>
    Prepared = True
    Left = 374
    Top = 7
  end
end
